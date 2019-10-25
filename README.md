# ATM-Simulation-II

edit on github

### Evolving your ATM simulation

We were using a fixed set of accounts and keeping them into memory. As we're evolving our app, we'll need to adjust it for a few requirements

- Read the account initial info from a csv file
  - Fields will remain Name, PIN, Balance, Account Number
- CSV file needs to have at least 20 records. Be creative :-)
- In order to read from CSV file, apply everything you've learned so far in this unit.
- We can safely assume that the CSV file will be on your disk, so you can pass an absolute path to read it
- Add the following validations:
  - There can't be 2 different accounts with the same Account Number
  - There can't be duplicated records
- We need to keep track of the transactions that have been made in our system. For that reason, model something that will help us with this requirement
- Provide a screen that help us query the last 10 transactions of a given account
- Use class/objects/methods names according to the DOMAIN. REFACTOR IF NECESSARY

### HINT!

You can make use of everything you've learned to this point. Special attention to Lambdas, Streams and I/O

### Review

Each person will randomly review at least two of other person’s work.

Follow the steps below:

- Run the application. Is the Welcome screen displayed?
- Does the application provide all the previous functionalities?
- Does the application provide a menu for loading a file? (i.e. passing an absolute path)
- Does the app allow to transfer to any of the accounts loaded from the file?
- Does the app provide a way of reading a csv record into an account? How cluttered is the code?
- Are there at least 3 abstractions?
  - Expected here would be to have:
    - Account class representation
    - Transaction class representation (along with some children like Withdraw, Transfer, Inquiry or anything that allows us to classify them)
    - Repository for reading from CSV file. A "pseudo"-DAO would be enough
    - Other domain classes are ok
- Does the code show any way of validating uniqueness of an account
- Does the app provide a way of querying last X transactions
- Does the app implement a way of reading the CSV file via Streams and I/O?

