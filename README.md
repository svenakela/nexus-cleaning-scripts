# nexus-cleaning-scripts

Groovy based cleaning script to remove all artifacts except last n objects

## Cleaning in Nexus

Nexus has some simple cleaning built in. If you, like me, have artifacts that are seldomly deployed this are at risk being removed despite being the latest artifact in use.

### Setup

- In Nexus settings, go to the Tasks dialog. Create a new `Execute Script`.
- Choose `Groovy` as language
- Add the script in the source field
- Change the settings in the top of the script
- Set your `Task Frequency`
- Save and have a beer.
