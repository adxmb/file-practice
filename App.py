# printHelp function prints the list of commands
def printHelp():
  print("\nCommands:")
  print("  help - Prints this list of commands.")
  print("  add - Adds new data into the file.")
  print("  remove - Removes specific data into the file.")
  print("  find - prints the information of a datapoint if the data point is found.")
  print("  edit - Edits a datapoint if the data point is found.")
  print("  exit - Exits the program.\n")
  return

# add function adds new data into the file
def add():
  return

# remove function removes specific data into the file
def remove():
  return

# find function prints the information of a datapoint if the data point is found
def find():
  return

# edit function edits a datapoint if the data point is found
def edit():
  return

# Main function
printHelp()
command = input().lower().strip()
while command != "exit":
  match command:
    case "help":
      printHelp()
    case "add":
      add()
    case "remove":
      remove()
    case "find":
      find()
    case "edit":
      edit()
    case _:
      print("Invalid command. Type 'help' to see the list of commands.")
  command = input().lower().strip()