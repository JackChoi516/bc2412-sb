# bc2412SB
- 2. Get Beans from Context (Resolve Dependencies between beans)
  - @Autowired on Class Attribute (Feild Injection)
    - "Spring Manager" resolve this dependency by finding an appropriate object fit into the attribute type
  - @Autowired on Constructor (Constructor Injection)
- 3. Flow
  - Controller Bean always @Autowired Service Bean
  - Service Bean always @Autowired Repository Bean
  - if "Spring Manager" cannot find any depenency, server start will fail.
- 4. RESTful API (GET/POST/DELETE/PUT/PATCH)
  - GET: Without create, update, or delete on resource
  - POST: Create resource ONLY
  - DELETE: Delete resourcesONLY (by id, or other resource attribute)
  - PUT: Make sure target resoucrce already exists (Find by id). Then replace the resource by the new resource directly.
  - PATCH: Make sure target resource already exists (Find by id). Then revies the target object attribute, but not replace the object

## Spring Boot Project Development

<!-- <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
      <scope>runtime</scope>
    </dependency> -->