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
  data = askForData()
  if data.lower() == "exit":
    return "exit"
  return

# askForData function asks for the data to be added
def askForData():
  line = ""
  given_name = input("  Given name:\n").capitalize().strip()
  if (given_name.lower() == "exit"):
    return "exit"
  line += given_name + " "

  surname = input("  Surname:\n").capitalize().strip()
  if (surname.lower() == "exit"):
    return "exit"
  line += surname + " "

  while (True):
    dob = input("  Date of birth (DD/MM/YYYY):\n").strip()
    if (dob.lower() == "exit"):
      return "exit"
    if (len(isValidDate(dob)) > 0):
      line += isValidDate(dob)
      break
    else:
      print("  Invalid date of birth. (Please use numbers and '/' only)")

  return line

# isValidDate function checks if the date is valid
def isValidDate(dob):
  parts = dob.split("/")
  if (len(parts) == 3):
    day = parts[0]
    month = parts[1]
    year = parts[2]
    if (day.isdigit() and month.isdigit() and year.isdigit()):
      if (int(day) > 0 and int(day) < 32 and int(month) > 0 and int(month) < 13 and int(year) > 0):
        date = ""
        if (int(day) < 10):
          date += "0" + day + "/"
        if (int(month) < 10):
          if (not checkMonthDay(int(month), int(day))):
            return ""
          date += "0" + month + "/"
        if (int(year) < 10):
          date += "0" + year
        elif (int(year) < 100):
          date += "00" + year
        elif (int(year) < 1000):
          date += "0" + year
        else:
          date += year
        return date
  return ""

# checkMonthDay function checks if the day is valid for the month
def checkMonthDay(month, day):
  if (month == 2):
    if (day > 29):
      return False
  elif (month == 4 or month == 6 or month == 9 or month == 11):
    if (day > 30):
      return False
  return True

# remove function removes specific data into the file
def remove():
  return

# find function prints the information of a datapoint if the data point is found
def find():
  return

# edit function edits a datapoint if the data point is found
def edit():
  return

# main function
def main():
  printHelp()
  command = input().lower().strip()
  while command != "exit":
    match command:
      case "help":
        printHelp()
      case "add":
        if add() == "exit":
          return
      case "remove":
        remove()
      case "find":
        find()
      case "edit":
        edit()
      case _:
        print("Invalid command. Type 'help' to see the list of commands.")
    command = input().lower().strip()
  return

main()