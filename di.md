# Dependency Injection — Notes

---

# Part 1: The Simple Explanation (Pizza Analogy)

## Pizza shop analogy

You order a pizza. You don't:
- grow the wheat
- milk the cow for cheese
- build the oven

You just say **"I need a pizza"** and someone hands you one. You *depend* on a pizza, but you don't *make* it. Someone else *injects* it into your hands.

That's it. That's Dependency Injection.

---

## In code terms

**A "dependency"** = anything your class needs to do its job.

If `OrderService` needs to send emails, then `EmailService` is its dependency.

**Two ways to get a dependency:**

### ❌ The bad way — make it yourself
```java
class OrderService {
    EmailService email = new EmailService();  // I built it myself
}
```
You're the chef, the farmer, AND the customer. Exhausting.

### ✅ The good way — ask for it
```java
class OrderService {
    EmailService email;

    OrderService(EmailService email) {   // "give me one"
        this.email = email;
    }
}
```
Someone else hands it to you. You just use it.

---

## Where does Spring fit in?

Spring is the **waiter**.

- You tag your classes with `@Component` = "hey Spring, this is on the menu."
- Spring builds one of each on startup and keeps them on a tray.
- When any class asks for an `EmailService` in its constructor, Spring grabs one from the tray and hands it over.

You never write `new EmailService()`. Spring does it for you. Done.

---

**Quick check:** in the pizza analogy:
- Spring = the waiter
- `OrderService` = you (the customer)
- `EmailService` = the pizza

---

# Part 2: The Full Picture

## 1. What DI actually is

Without DI:
```java
class OrderService {
    private EmailService email = new EmailService();
}
```
`OrderService` is **glued** to `EmailService`. Hard to swap, hard to test.

With DI:
```java
class OrderService {
    private final EmailService email;
    OrderService(EmailService email) { this.email = email; }
}
```
Spring builds `EmailService` and **hands it to you**. That's "injection." Spring controls creation, not you. That's **Inversion of Control (IoC)**.

## 2. `@Component` + `@Autowired`

- `@Component` on a class = "Spring, manage this. Keep one in your container (the `ApplicationContext`)."
- Variants: `@Service`, `@Repository`, `@Controller` — same thing, different labels.
- Spring scans on startup, finds every `@Component`, builds them. Each is a **bean**.
- `@Autowired` = "find one of these in the container and plug it in here."

```java
@Component
class EmailService { ... }

@Component
class OrderService {
    @Autowired
    private EmailService email;
}
```

## 3. Three injection styles

```java
// Constructor (BEST)
@Component
class OrderService {
    private final EmailService email;
    OrderService(EmailService email) { this.email = email; }
}

// Field
@Component
class OrderService {
    @Autowired private EmailService email;
}

// Setter
@Component
class OrderService {
    private EmailService email;
    @Autowired void setEmail(EmailService e) { this.email = e; }
}
```
**Why constructor wins:** fields can be `final`, can't forget a dependency, easy to unit-test (`new OrderService(mock)`), no Spring magic in tests.

Since Spring 4.3, `@Autowired` is optional on a single constructor.

## 4. `@Qualifier` and `@Primary`

Two beans implement the same interface?
```java
interface PaymentGateway {}
@Component class Stripe implements PaymentGateway {}
@Component class Paypal implements PaymentGateway {}

@Component
class Checkout {
    Checkout(PaymentGateway pg) {...}  // which one?? Spring throws.
}
```
Fixes:
```java
// Option A: default
@Component @Primary class Stripe implements PaymentGateway {}

// Option B: name it
Checkout(@Qualifier("paypal") PaymentGateway pg) {...}
```

## 5. Scopes

- `singleton` (default): **one** instance for the whole app.
- `prototype`: **new** instance every time.
```java
@Component
@Scope("prototype")
class ShoppingCart {}
```
99% of the time use singleton.

---

**Mental model:** Spring is a big `Map<Type, Object>`. On startup it fills the map by scanning `@Component`s. When a bean needs another bean, Spring looks it up and passes it in. That's it.
