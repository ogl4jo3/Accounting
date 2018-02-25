# Accounting
記帳本

play store:
https://play.google.com/store/apps/details?id=ogl4jo3.shaowei.ogl4jo3.accounting&hl=zh_TW

command line read database
1. adb devices
2. emulator:
	 adb -s emulator-xxxx shell
	 device:
	 adb -d{<serialNumber> shell
3. cd data/data/<your-package-name>/databases/
4. sqlite3 <your-db-name>.db
5. Select * from table1 where ...;

List the tables in your database:
.tables
List how the table looks:
.schema tablename
List all of the available SQLite prompt commands:
.help
