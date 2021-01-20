# Larynx

A NPC dialogue library for plasmid minigames.

Example usage:

`MyModFile.java`
```java
TrackRegistry.INSTANCE
        .get(new Identifier("larynx", "mytrack"))
        .play(context.getSource().getPlayer());
```

`data/larynx/dialogues/mytrack.json`
```json
{
  "lines": [
    {
      "type": "usual",
      "text": "Hello! Fine day today isn't it?",
      "author": "NPC 1",
      "delay": 30,
      "color": "blue"
    },
    {
      "type": "usual",
      "text": "I know right! Blue skies all around!",
      "author": "NPC 2",
      "color": "red"
    }
  ],
  "defaultDelay": 60,
  "id": "larynx:mytrack"
}
```