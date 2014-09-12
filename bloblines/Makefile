BUILD_DIR=bin
SOURCE_DIR=src
LIBS_DIR=lib

# Rebuild when a file has changed
DEPS_DIRS=$(shell find ${SOURCE_DIR} -type d)

JAVAC=javac -encoding iso8859-15 -d ${BUILD_DIR} -sourcepath ${SOURCE_DIR} -extdirs lib
JAVA=java -classpath bin:lib/*


JAR=bloblines.jar

all: compile

compile: ${DEPS_DIRS}
	find ${SOURCE_DIR} -name '*.java' > compile
	rsync -a assets/ ${BUILD_DIR}/
	${JAVAC} '@compile'

run: compile
	${JAVA} org.bloblines.desktop.DesktopLauncher

clean:
	rm -rf bin/* compile

.PHONY: all run clean
