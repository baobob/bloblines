BUILD_DIR=bin
SOURCE_DIR=src
TEST_DIR=test
WEB_DIR=web
LIBS_DIR=lib

# Rebuild when a file has changed
DEPS_DIRS=$(shell find ${SOURCE_DIR} -type d)

JAVAC=javac -d ${BUILD_DIR} -sourcepath ${SOURCE_DIR} -extdirs lib
JAVA=java -classpath bin:lib/*


JAR=bloblines.jar

all: compile

compile: ${DEPS_DIRS}
	find ${SOURCE_DIR} -name '*.java' > compile
	find ${TEST_DIR} -name '*.java' >> compile
	find ${WEB_DIR} -name '*.java' >> compile
	${JAVAC} '@compile'

run: compile
	${JAVA} org.bloblines.test.BloblinesUi

.PHONY: all run
