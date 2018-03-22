# IOTA MAM LIBRARY

## THIS LIBRARY IS WORKING PROGRESS. IT IS NOT COMPILE YET. IT IS NOT TESTED

I am trying to translate old js library to java language

Note to compile:
* Import jota lib

mvn install:install-file -Dfile=jota-0.9.11-SNAPSHOT.jar -DgroupId=com.github.iotaledger -DartifactId=iota.lib.java -Dversion=0.9.11-SNAPSHOT -Dpackaging=jar

* Copied class from iri

  - com.iota.iri.hash.Curl
  - com.iota.iri.hash.Kerl
  - com.iota.iri.hash.Sponge
  - com.iota.iri.hash.SpongeFactory