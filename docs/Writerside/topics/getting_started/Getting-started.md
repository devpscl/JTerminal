# Getting started

To use jterminal in your project, you should choose the correct modules 
and adds the following dependency and repository to your project (Maven, Gradle)


| Module                 | Description                                                                                                                        |
|------------------------|:-----------------------------------------------------------------------------------------------------------------------------------|
| [Core](Core-Module.md) | The core module has all the relevant terminal features that this library brings with it and is required for all additional modules |
| [CLI](CLI-Module.md)   | The commandline module allows to create simple command line terminals and define your own commands                                 |
| [UI](UI-Module.md)     | the user-interface module comes with a whole toolkit to create simple user-friendly gui in your own terminal window                |

The core of the library is executed native depending on the system. It is also supported without native access,
but this is <b>not recommended</b> as the functionality is severely limited.

## Supported systems on native execution
| OS    | Arch            | Supported |
|-------|-----------------|-----------|
| Win   | Amd64 / x86_64  | ✅         |
| Linux | Amd64 / x86_64  | ✅         |
| Linux | Aarch64 / Arm64 | ✅         |
| Macos | Aarch64 / Arm64 | ✅         |



