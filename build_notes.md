# Building Blast the Box

Install the JDK 19 (as of writing 7/2/2023, Kotlin Gradle Plugin has a bug and will not work with JDK 20)
1.	Clone `blast_the_box` from https://github.com/nrmiller/blast_the_box.git
2.	Clone `fractal-dungeon` from https://github.com/nrmiller/fractal-dungeon.git

    Make sure to clone it into the `blast_the_box/fractal-dungeon` folder

*This project uses git submodules. You may need to initialize the `fractal-dungeon` repository.

In the root directory `blast_the_box`, from command-line, execute the assemble task using the Gradle Wrapper:

    gradlew.bat assemble

To create an exe, execute the following Gradle task:

    gradlew.bat desktop:createExe

## Common Issues

> Execution failed for task ':buildSrc:compileKotlin'.

**Solution:**

Kotlin could not find the required JDK tools in the Java installation. Make sure Kotlin compilation is running on a JDK, not JRE.