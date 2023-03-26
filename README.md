# ADSS GROUP K 

## Members

| Name             
|------------------
| Miri Volozhinsky 
| Batel Shkolnik   
| Yuval Dolev      
| Michael Tzahi    
| Shoham Cohen     
| Lior Kummer      
| Ido Shapira      
| Tamir Avisar     

## Development

For IntelliJ users:

```
git clean -d -X --force
cd dev
mvn idea:idea
```

Double click on `dev/adss.ipr` to open the IntelliJ project.

### Packaging

Build a Jar:

```
mvn clean package shade:shade
```

Run the Jar:

```
java -jar ./target/`adss-0.0.1.jar
```
