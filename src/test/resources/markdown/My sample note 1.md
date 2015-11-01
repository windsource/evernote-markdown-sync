# something new

# Resources
* [SQL tutorial from W3Schools.com](http://www.w3schools.com/sql)

# Examples
## Simple query
``` sql
SELECT column_name,column_name FROM table_name
```

## Distinct rows
``` sql
SELECT DISTINCT column_name,column_name FROM table_name
```

## Where
``` sql
SELECT * FROM Customers WHERE Country='Germany' AND City='Berlin';
```

## Order
``` sql
SELECT column_name,column_name
FROM table_name
ORDER BY column_name,column_name ASC|DESC;
```
``` sql
SELECT * FROM Customers
ORDER BY Country,CustomerName;
```

## Insert
``` sql
INSERT INTO table_name
VALUES (value1,value2,value3,...);
```
``` sql
INSERT INTO table_name (column1,column2,column3,...)
VALUES (value1,value2,value3,...);
```

## Aliases
Aliases for columns
``` sql
SELECT column_name AS alias_name
FROM table_name;
```
Aliases for tables
``` sql
SELECT column_name(s)
FROM table_name AS alias_name;
```

## Joins
### Inner joins
The most common type of join is: SQL INNER JOIN (simple join). An SQL INNER JOIN return all rows from multiple tables where the join condition is met.
``` sql
SELECT Orders.OrderID, Customers.CustomerName, Orders.OrderDate
FROM Orders
INNER JOIN Customers
ON Orders.CustomerID=Customers.CustomerID; 
```

Join more than two tables:
``` sql
SELECT t1.col, t3.col FROM table1 inner join table2 ON table1.primarykey = table2.foreignkey
                                  inner join table3 ON table2.primarykey = table3.foreignkey
```

