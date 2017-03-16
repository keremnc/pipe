# pipe

# Commands
Commands implement the UserAction class. There are existing abstract classes that already implement the UserAction class that can be used, such as NoArgsAction, and WildcardArgsAction.

Command registration is done thru Pipeline.USER_ACTIONS. It is a map where the key is the command number.

Data and parameters are passed thru to a UserAction from the PipelineContext class, which has a PipeSystem & String[].

# Graph

PipeSystem is the extension of the Graph class. There exists a serialize() in Graph, and is extended in PipeSystem to allow for saving the history of recently deleted pipes as well.

# Images
## Main Menu
![main_menu](https://cloud.githubusercontent.com/assets/3498221/23890372/d5fad786-084e-11e7-9e79-171a653fa0d7.png)

## Command usage
![p1](https://cloud.githubusercontent.com/assets/3498221/23890377/ddc18d98-084e-11e7-8daf-7b326aa5329a.png)
![p2](https://cloud.githubusercontent.com/assets/3498221/23890378/de8d63dc-084e-11e7-9523-3d2eef2df406.png)

