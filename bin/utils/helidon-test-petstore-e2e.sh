#!/bin/bash
#
# A script to test all Helidon generators end-to-end
#

executable="./modules/openapi-generator-cli/target/openapi-generator-cli.jar"
logfile="/tmp/helidon-test-petstore-e2e-output.log"
#apifile="/tmp/petstore.yaml"
project="/tmp/openapi-generator-petstore"
apifile="modules/openapi-generator/src/test/resources/3_0/petstore-with-fake-endpoints-models-for-testing.yaml"

function help() {
  echo "$0 [generator]"
  echo "If no generator specified, all Helidon generators found are tested"
  exit 0
}

function main() {
#setup

  if [ -z ${generator} ]; then
    # Find and test all Helidon generators
    for generator in $(java -jar ${executable} list --short | sed -e 's/,/\'$'\n''/g' | grep helidon)
    do
      verify ${generator}
    done
  else
    verify ${generator}
  fi
}

function verify() {
  echo "Using ${1}"
  
  echo "Generating project from ${apifile} ..."
  if eval java -jar ${executable} generate -i ${apifile} -g ${1} -o ${project} > ${logfile} 2>&1; then
    echo "Project generated successfully using ${1}"
  else
    echo "ERROR: Failed to run '${1}' generator. The command was:"
    echo "java -jar ${executable} generate -i ${apifile} -g ${1} -o ${project}"
    echo "ERROR: The output of the command was:"
    cat ${logfile}
    exit 1
  fi

  echo "Building generated project ..."
  cd ${project} || exit 1
  if eval mvn clean install -DskipTests; then
    echo "Project compiled successfully"
  else
    echo "ERROR: Compiling project ${project}"
    exit 2
  fi

  echo "Project location is ${project}"
}

function setup() {
  rm -rf ${project}

  echo '
openapi: "3.0.0"
info:
  version: 1.0.0
  title: Swagger Petstore
  license:
    name: MIT
servers:
  - url: http://petstore.swagger.io/v1
paths:
  /pets:
    get:
      summary: List all pets
      operationId: listPets
      tags:
        - pets
      parameters:
        - name: limit
          in: query
          description: How many items to return at one time (max 100)
          required: false
          schema:
            type: integer
            format: int32
      responses:
        200:
          description: An paged array of pets
          headers:
            x-next:
              description: A link to the next page of responses
              schema:
                type: string
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Pets"
        default:
          description: unexpected error
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
    post:
      summary: Create a pet
      operationId: createPets
      tags:
        - pets
      responses:
        201:
          description: Null response
        default:
          description: unexpected error
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
  /pets/{petId}:
    get:
      summary: Info for a specific pet
      operationId: showPetById
      tags:
        - pets
      parameters:
        - name: petId
          in: path
          required: true
          description: The id of the pet to retrieve
          schema:
            type: string
      responses:
        200:
          description: Expected response to a valid request
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Pets"
        default:
          description: unexpected error
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/Error"
components:
  schemas:
    Pet:
      required:
        - id
        - name
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
        tag:
          type: string
    Pets:
      type: array
      items:
        $ref: "#/components/schemas/Pet"
    Error:
      required:
        - code
        - message
      properties:
        code:
          type: integer
          format: int32
        message:
          type: string
' > ${apifile}
}

if [ $# -gt 0 ];
then
  if [ $1 = "--help" ]; then
    help
  fi
  generator=$1
fi

main
