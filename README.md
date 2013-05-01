Frankees and data generation patterns
==

```
  _____ _____ _____ ______ _____ _____ _____ _____ 
 |   __|  _  |  _  |   |  |  |  |   __|   __|   __|
 |   __|    -|     |      |    -|   __|   __|__   |
 |__|  |__|__|__|__|__|___|__|__|_____|_____|_____|
                                           PATTERNS
```

Test data generation is something everyone is doing each time he writes some unit test. The given phase of a given/when/then test is especially the place where you do it.

```java
@Test
public void testRunBattle() {
    // Given
    MonsterBattle battle = new MonsterBattle();
    Monster dracula, wolfman; // Need some test data here

    // Then
    assertThat(battle.runBattle(dracula, wolfman)).isEqualTo(wolfman);
}
```

As you can find around the web, there are mostly two patterns in test data generation. The first one advise to group all your created objects in one place, the other uses classes to ease object creation.

You'll find, in [the *patterns* branch](https://github.com/ruaudl/frankees/tree/patterns), some simple examples demonstrating both patterns.

# Object Mother

Object mother acts as a factory to provide several use cases. These objects are standard data that can be used in many test cases.

```java
@Test
public void testRunBattle() {
    // Given
    MonsterBattle battle = new MonsterBattle();
    Monster dracula = Monsters.getDracula();
    Monster wolfman = Monsters.getWolfman();

    // Then
    assertThat(battle.runBattle(dracula, wolfman)).isEqualTo(wolfman);
}

@Test
public void testFrightBattle() {
    // Given
    MonsterBattle battle = new MonsterBattle();
    Monster dracula = Monsters.getDracula();
    Monster wolfman = Monsters.getWolfman();

    // Then
    assertThat(battle.frightBattle(dracula, wolfman)).isEqualTo(dracula);
}
```

In the example above, we choose to code our object mother in an utility class (with static methods).

```java
public static Monster getDracula() {
    Monster dracula = new Monster();
    dracula.setName("Dracula");
    dracula.setLegsCount((short) 2);
    dracula.setFrightLevel(10);
	
    return dracula;
}
```

# Data Builder

Data builder provides a fluent way to  build an object (like any other builder). This makes it more flexible as you can build the exact object you need for each test.

```java
@Test
public void testRunBattle() {
    // Given
    MonsterBattle battle = new MonsterBattle();
    Monster dracula = MonsterBuilder.aMonster().withLegsCount(2).build();
    Monster wolfman = MonsterBuilder.aMonster().withLegsCount(4).build();

    // Then
    assertThat(battle.runBattle(dracula, wolfman)).isEqualTo(wolfman);
}

@Test
public void testFrightBattle() {
    // Given
    MonsterBattle battle = new MonsterBattle();
    Monster dracula = MonsterBuilder.aMonster().withFrightLevel(10).build();
    Monster wolfman = MonsterBuilder.aMonster().withFrightLevel(8).build();

    // Then
    assertThat(battle.frightBattle(dracula, wolfman)).isEqualTo(dracula);
}
```

Builders are usually implemented with an utility class but you can find some directly implemented in the test class.

```java
public final class MonsterBuilder {

    private Monster store;

    private MonsterBuilder() {
        store = new Monster();
    }

    public static MonsterBuilder aMonster() {
        return new MonsterBuilder();
    }

    public MonsterBuilder withName(String name) {
        store.setName(name);
        return this;
    }

    // (...) Many other methods

    public Monster build() {
        Monster builded = new Monster();
        builded.setName(store.getName());
        builded.setFrightLevel(store.getFrightLevel());
        builded.setLegsCount(store.getLegsCount());
        return builded;
    }
}
```