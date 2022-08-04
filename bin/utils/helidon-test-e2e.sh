#!/bin/bash
#
# A script to test Helidon generators end-to-end
#

dir=`pwd`
executable="${dir}/modules/openapi-generator-cli/target/openapi-generator-cli.jar"
logfile="/tmp/helidon-test-e2e-output.log"
project="/tmp/openapi-generator"

# E2E testing for Server MP
java_helidon_server_mp="
modules/openapi-generator/src/test/resources/3_0/helidon/petstore-for-testing.yaml
modules/openapi-generator/src/test/resources/3_0/petstore-with-fake-endpoints-models-for-testing.yaml
"

# E2E testing for Server SE
java_helidon_server_se="
modules/openapi-generator/src/test/resources/3_0/helidon/petstore-for-testing.yaml
"

# E2E testing for Client MP
java_helidon_client_mp="
modules/openapi-generator/src/test/resources/3_0/petstore-with-fake-endpoints-models-for-testing.yaml
modules/openapi-generator/src/test/resources/3_0/helidon/petstore-no-multipart-for-testing.yaml
"

# E2E testing for Client SE
java_helidon_client_se="
modules/openapi-generator/src/test/resources/3_0/helidon/petstore-for-testing.yaml
"

function help() {
  echo "$0 (java-helidon-server|java-helidon-client) (mp|se)"
  exit 1
}

function verify() {
  apifiles=`echo "${1}-${2}" | sed 's/-/_/g'`

  for apifile in ${!apifiles}
  do
      echo "Generating project for ${apifile} ..."

      ls ${project} >/dev/null 2>&1 && rm -rf ${project}

      if eval java -jar ${executable} generate --input-spec ${apifile} --generator-name ${1} --library ${2} -o ${project} > ${logfile} 2>&1; then
        echo "Project generated successfully using ${1} ${2}"
        cp ${apifile} ${project}
      else
        echo "ERROR: Failed to run '${1}' generator. The command was:"
        echo "java -jar ${executable} generate -i ${apifile} -g ${1} -o ${project}"
        echo "ERROR: The output of the command was:"
        cat ${logfile}
        cp ${apifile} ${project}
        exit 1
      fi

      echo "Building generated project ..."
      cd ${project} || exit 1
      if eval mvn clean package -DskipTests; then
        echo "Project compiled successfully"
      else
        echo "ERROR: Compiling project ${project}"
        exit 2
      fi
      cd ${dir} || exit 1

      echo "========================================================================"
  done
}

if [ $# -gt 0 ];
then
  if [ "$1" == "--help" ];
  then
    help
  fi
  if [ $# -eq 2 ];
  then
    generator="$1"
    if [ "$generator" != "java-helidon-server" ] && [ "$generator" != "java-helidon-client" ];
    then
      help
    fi
    library="$2"
    if [ "$library" != "mp" ] && [ "$library" != "se" ];
    then
      help
   fi
  verify "$generator" "$library"
  exit 0
  fi
fi

help
