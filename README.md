# Larynx

A NPC dialogue library for plasmid minigames.

Example usage:

`MyModFile.java`
```java
Larynx.get(new Identifier("myMod", "mytrack"), myMod.class).play(player);
```

`assets/larynx/dialogues/mytrack.json`
```json
{
  "dialogues": [
    {
      "type": "usual",
      "text": "Hello! Fine day today isn't it?",
      "author": "NPC 1",
      "delay": "30",
      "color": "blue"
    },
    {
      "type": "usual",
      "text": "I know right! Blue skies all around!",
      "author": "NPC 2",
      "color": "red"
    }
  ],
  "ticksBetween": 60
}
```