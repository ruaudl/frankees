Maven plugin for frankees-generation-databuilder
==

This plugin can generate [Frankees databuilder](https://github.com/ruaudl/frankees) for your own classes via maven (it is based on [assertj-assertions-generator](https://github.com/ruaudl/frankees)).

Let's say that you have a `Player` class with `name` and `team` attributes, the plugin is able to create a `PlayerBuilder` databuilder class with `withName` and `withTeam` methods, to write code like :

```java
PlayerBuilder.aPlayer().withName('Messi').withTeam('Barcelone').build();
```

The plugin can be launched with command `mvn generate-test-sources` (or simply `mvn test`) or with any IDE that supports maven.
By default, it generates the assertions source files in `target/generated-test-sources/databuilders` as per maven convention (but this can be changed - see below).

Releases
--

**2013-04-20 : **

Configuration
--

To generate databuilder, add the following plugin to your `pom.xml` build/plugins section :

```xml
<plugin>
    <groupId>org.frankees</groupId>
    <artifactId>frankees-maven-plugin-databuilder</artifactId>
    <version>1.0.0</version>
    <configuration>
        <packages>
            <param>your.first.package</param>
            <param>your.second.package</param>
            ...
        </packages>
    </configuration>
</plugin>
```

... and execute the command :
```
mvn frankees:databuilder
```

`packages` configuration element is required so that the plugin knows where to find classes you want to generate databuilder for.
You can use `classes` configuration element too. It works as `packages`configuration element.

```xml
<configuration>
    <classes>
        <param>your.first.class</param>
        <param>your.second.class</param>
        ...
    </classes>
</configuration>
```


You can also specify non-standard destination directory for databuilder files using `targetDir` configuration element e.g.

```xml
<configuration>
    <packages>
        <param>your.first.package</param>
        <param>your.second.package</param>
        ...
    </packages>
    <targetDir>YOUR_NON_STANDARD_DIR</targetDir>
</configuration>
```

To generate databuilder classes at every build, add an `<executions>` section as shown below :

```xml
<plugin>
    <groupId>org.frankees</groupId>
    <artifactId>frankees-maven-plugin-databuilder</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <goals>
                <goal>databuilder</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <packages>
            <param>your.first.package</param>
            <param>your.second.package</param>
            ...
        </packages>
    </configuration>
</plugin>
```

