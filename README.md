# pipe

# Commands
Commands implement the UserAction class. There are existing abstract classes that already implement the UserAction class that can be used, such as NoArgsAction, and WildcardArgsAction.

Command registration is done thru Pipeline.USER_ACTIONS. It is a map where the key is the command number.

Data and parameters are passed thru to a UserAction from the PipelineContext class, which has a PipeSystem & String[].

# Graph

PipeSystem is the extension of the Graph class. There exists a serialize() in Graph, and is extended in PipeSystem to allow for saving the history of recently deleted pipes as well.


