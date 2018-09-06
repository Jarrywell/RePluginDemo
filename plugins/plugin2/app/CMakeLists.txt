# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# 配置so库信息
add_library( # Sets the name of the library.
             # 生成的so库名称，此处生成的so文件名称是libplugin-string.so
             plugin-string

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # 资源文件，可以多个，
             # 资源路径是相对路径，相对于本CMakeLists.txt所在目录
             src/main/jni/plugin-string.c )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# 从系统查找依赖库
find_library( # Sets the name of the path variable.
              # android系统每个类型的库会存放一个特定的位置，而log库存放在log-lib中
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              # android系统在c环境下打log到logcat的库
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 配置库的链接（依赖关系）
target_link_libraries( # Specifies the target library.
                       plugin-string

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )