Quest Gen 
=========
Procedurally generated text-based (Interactive Fiction) adventure game in the style of Zork or The Colossal Cave Adventure.
Group Members: David Cottrell, Avery Morin

Goal:
-----
Generate a fully navigable world containing trading quests (explained further in the narrative analysis section) 
that are guaranteed to be solveable.  These quests are chained, leading to victory!

Generate a game where the goal is to complete a sequence of "trades" with NPCs in order to reach some goal item.
We want to show that we can use this simple structure to generate a typical story line, which we encode as a 'trade' system. This is basically a restricted petri net.

Narrative analysis/generation
-----------------------------
	Generate game quests lines and NPC interactions that are reasonable. We want to define acceptable trades for NPCs.
	We can generate the quest line as a tree, by considering each NPC as a node with edges defined by what they can accept or give as a result of a trade.
	Note that NPC in this case is a generalized concept, including doors and chests and dragons to be slain!
	To build this tree, we choose a goal item, and randomly walk through the graph from this point. This will give us a sequence of trades and NPCs with which we can populate the world.

Procedural Content Generation
-----------------------------
	Generate a coherent map and NPC placement. The world needs to be interesting, and have different regions with believable transitions between them.
	This involves constructing a world around the NPCs that the narrative has deemed necessary.  The NPCs need to be placed in the world in a way such that they are all accessible when they are necessary for a trade.  Ideally they are also placed in environments that match their class.

	The NPCs and environments (descriptions) will also need to be generated.  The current plan is to do this by defining classes of NPCs that will give and take items from set lists.  Each class can also be easily associated with a class of environment.
	Examples of NPC classes: 
			Townsperson, Shopkeeper, Adventurer, Monster, CraftingStation, Container, Door
	Examples of Environment classes: 
			Desert, Street, Jungle, Forest, Space, Cliff, House, Ship, Shop, Water, Well
	
Separation of Work:
-------------------
The topics are already fairly well separated.
- Build a quest node generator
- Build the quest graph
- Build the world from the quest graph
- Make the world navigable by the player (make it a game)
