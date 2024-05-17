package com.martincastroalvarez.hex.hex.utils;

import com.martincastroalvarez.hex.hex.domain.ports.out.*;
import com.martincastroalvarez.hex.hex.domain.models.*;
import com.martincastroalvarez.hex.hex.domain.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Random;

@Component
public class DataSeed implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private PtoRepository ptoRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private AuthenticationService authenticationService;

    Random random = new Random();

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = createUser(1, "User", "user@test.com", User.Role.USER);
        User manager = createUser(2, "Manager", "manager@test.com", User.Role.MANAGER);
        User admin = createUser(3, "Admin", "admin@test.com", User.Role.ADMIN);
        User salesman = createUser(4, "Salesman", "salesman@test.com", User.Role.SALESMAN);
        User provider = createUser(5, "Provider", "provider@test.com", User.Role.PROVIDER);

        Product product1 = createProduct(1, "Product 1", 10.0);
        Product product2 = createProduct(2, "Product 2", 50.0);
        Product product3 = createProduct(3, "Product 3", random.nextDouble() * 100);
        Product product4 = createProduct(4, "Product 4", random.nextDouble() * 100);
        Product product5 = createProduct(5, "Product 5", random.nextDouble() * 100);
        Product product6 = createProduct(6, "Product 6", random.nextDouble() * 100);
        Product product7 = createProduct(7, "Product 7", random.nextDouble() * 100);
        Product product8 = createProduct(8, "Product 8", random.nextDouble() * 100);

        Meeting meeting1 = createMeeting(1, "Weekly Sync", LocalDateTime.now().plusDays(1));
        Meeting meeting2 = createMeeting(2, "Weekly Sync", LocalDateTime.now().plusDays(2));
        Meeting meeting3 = createMeeting(3, "Weekly Sync", LocalDateTime.now().plusDays(3));
        Meeting meeting4 = createMeeting(4, "Performance Review", LocalDateTime.now().plusDays(90));
        Meeting meeting5 = createMeeting(5, "Project Kickoff", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting6 = createMeeting(6, "Client Update", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting7 = createMeeting(7, "Budget Review", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting8 = createMeeting(8, "Quarterly Planning", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting9 = createMeeting(9, "Team Outing", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting10 = createMeeting(10, "Tech Talk", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting11 = createMeeting(11, "Safety Training", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting12 = createMeeting(12, "Holiday Party", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting13 = createMeeting(13, "Year End Review", LocalDateTime.now().plusDays(random.nextInt(60)));
        Meeting meeting14 = createMeeting(14, "New Hire Orientation", LocalDateTime.now().plusDays(random.nextInt(60)));

        inviteUserToMeeting(meeting1, user);
        inviteUserToMeeting(meeting1, manager);
        inviteUserToMeeting(meeting1, admin);
        inviteUserToMeeting(meeting1, salesman);
        inviteUserToMeeting(meeting1, provider);
        inviteUserToMeeting(meeting2, user);
        inviteUserToMeeting(meeting2, manager);
        inviteUserToMeeting(meeting3, manager);
        inviteUserToMeeting(meeting4, manager);
        inviteUserToMeeting(meeting5, manager);
        inviteUserToMeeting(meeting6, user);
        inviteUserToMeeting(meeting7, user);
        inviteUserToMeeting(meeting8, user);
        inviteUserToMeeting(meeting9, user);
        inviteUserToMeeting(meeting10, user);
        inviteUserToMeeting(meeting11, user);
        inviteUserToMeeting(meeting12, user);
        inviteUserToMeeting(meeting13, admin);
        inviteUserToMeeting(meeting14, user);

        createSchedule(1, user, "09:00", "17:00", DayOfWeek.MONDAY);
        createSchedule(2, admin, "09:00", "17:00", DayOfWeek.MONDAY);
        createSchedule(3, manager, "09:00", "17:00", DayOfWeek.MONDAY);
        createSchedule(4, provider, "09:00", "17:00", DayOfWeek.MONDAY);
        createSchedule(5, salesman, "09:00", "17:00", DayOfWeek.MONDAY);
        createSchedule(6, user, "09:00", "17:00", DayOfWeek.TUESDAY);
        createSchedule(7, admin, "09:00", "17:00", DayOfWeek.TUESDAY);
        createSchedule(8, manager, "09:00", "17:00", DayOfWeek.TUESDAY);
        createSchedule(9, provider, "09:00", "17:00", DayOfWeek.TUESDAY);
        createSchedule(10, salesman, "09:00", "17:00", DayOfWeek.TUESDAY);
        createSchedule(11, user, "09:00", "17:00", DayOfWeek.WEDNESDAY);
        createSchedule(12, admin, "09:00", "17:00", DayOfWeek.WEDNESDAY);
        createSchedule(13, manager, "09:00", "17:00", DayOfWeek.WEDNESDAY);
        createSchedule(14, provider, "09:00", "17:00", DayOfWeek.WEDNESDAY);
        createSchedule(15, salesman, "09:00", "17:00", DayOfWeek.WEDNESDAY);

        Message message1 = createMessage(1, manager, "Reminder", "Don't forget the meeting!", LocalDateTime.now());
        Message message2 = createMessage(2, admin, "Important", "This is important!", LocalDateTime.now());
        Message message3 = createMessage(3, manager, "Hi There", "Hello World!", LocalDateTime.now());
        Message message4 = createMessage(4, manager, "Update", "Please update the documents.", LocalDateTime.now().minusHours(random.nextInt(48)));
        Message message5 = createMessage(5, admin, "Deadline", "Deadline is approaching!", LocalDateTime.now().minusHours(random.nextInt(48)));
        Message message6 = createMessage(6, manager, "Sales Report", "Need the latest sales report.", LocalDateTime.now().minusHours(random.nextInt(48)));
        Message message7 = createMessage(7, admin, "Inventory", "Check the inventory levels.", LocalDateTime.now().minusHours(random.nextInt(48)));
        Message message8 = createMessage(8, manager, "Welcome", "Welcome to the team!", LocalDateTime.now().minusHours(random.nextInt(48)));

        addUserToMessage(message1, user);
        addUserToMessage(message1, admin);
        addUserToMessage(message2, user);
        addUserToMessage(message2, manager);
        addUserToMessage(message3, user);
        addUserToMessage(message3, manager);
        addUserToMessage(message4, user);
        addUserToMessage(message4, salesman);
        addUserToMessage(message5, user);
        addUserToMessage(message5, manager);
        addUserToMessage(message6, user);
        addUserToMessage(message6, manager);
        addUserToMessage(message7, user);
        addUserToMessage(message7, manager);
        addUserToMessage(message8, user);

        createPto(1, user, LocalDateTime.now().plusDays(10), Pto.Type.VACATION);
        createPto(2, user, LocalDateTime.now().plusDays(11), Pto.Type.VACATION);
        createPto(3, user, LocalDateTime.now().plusDays(13), Pto.Type.VACATION);
        createPto(4, user, LocalDateTime.now().plusDays(20), Pto.Type.PERSONAL);
        createPto(5, manager, LocalDateTime.now().minusDays(5), Pto.Type.SICK);
        createPto(6, salesman, LocalDateTime.now().plusDays(random.nextInt(30)), Pto.Type.values()[random.nextInt(Pto.Type.values().length)]);
        createPto(7, provider, LocalDateTime.now().plusDays(random.nextInt(30)), Pto.Type.values()[random.nextInt(Pto.Type.values().length)]);
        createPto(8, admin, LocalDateTime.now().plusDays(random.nextInt(30)), Pto.Type.values()[random.nextInt(Pto.Type.values().length)]);
        createPto(9, salesman, LocalDateTime.now().plusDays(random.nextInt(30)), Pto.Type.values()[random.nextInt(Pto.Type.values().length)]);
        createPto(10, provider, LocalDateTime.now().plusDays(random.nextInt(30)), Pto.Type.values()[random.nextInt(Pto.Type.values().length)]);

        createPurchase(1, product1, provider, 100.0, 500.0, LocalDateTime.now().minusDays(3));
        createPurchase(2, product1, provider, 100.0, 400.0, LocalDateTime.now().minusDays(2));
        createPurchase(3, product1, provider, 100.0, 300.0, LocalDateTime.now().minusDays(1));
        createPurchase(4, product3, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));
        createPurchase(5, product4, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));
        createPurchase(6, product5, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));
        createPurchase(7, product6, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));
        createPurchase(8, product7, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));
        createPurchase(9, product8, provider, 100.0, 100.0 + random.nextDouble() * 900, LocalDateTime.now().minusDays(random.nextInt(10)));

        createSale(1, product2, salesman, 50.0, 650.0, LocalDateTime.now().minusDays(3));
        createSale(2, product2, salesman, 50.0, 700.0, LocalDateTime.now().minusDays(2));
        createSale(3, product2, salesman, 50.0, 750.0, LocalDateTime.now().minusDays(1));
        createSale(4, product3, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));
        createSale(5, product4, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));
        createSale(6, product5, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));
        createSale(7, product6, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));
        createSale(8, product7, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));
        createSale(9, product8, salesman, 50.0, 500.0 + random.nextDouble() * 500, LocalDateTime.now().minusDays(random.nextInt(10)));

        createWork(1, user, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(4));
        createWork(2, user, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(4));
        createWork(3, user, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(4));
        createWork(4, manager, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(8));
        createWork(5, manager, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(8));
        createWork(6, manager, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(8));
        createWork(7, provider, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));
        createWork(8, provider, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(2));
        createWork(9, provider, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(2));
        createWork(10, salesman, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(2));
        createWork(11, salesman, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2).plusHours(2));
        createWork(12, salesman, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusHours(2));
        createWork(13, user, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(14, user, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(15, manager, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(16, manager, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(17, provider, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(18, provider, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(19, salesman, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(20, salesman, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(21, admin, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
        createWork(22, admin, LocalDateTime.now().minusDays(random.nextInt(10)), LocalDateTime.now().minusDays(random.nextInt(10)).plusHours(random.nextInt(8) + 1));
    }

    private User createUser(Integer id, String name, String email, User.Role role) {
        Optional<User> optional = userRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(authenticationService.encodePassword("test"));
        user.setSignUpDate(LocalDateTime.now());
        user.setLastLoginDate(user.getSignUpDate());
        user.setIsActive(true);
        user.setRole(role);
        return userRepository.save(user);
    }

    private Product createProduct(Integer id, String name, Double price) {
        Optional<Product> optional = productRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setIsActive(true);
        return productRepository.save(product);
    }

    private Meeting createMeeting(Integer id, String title, LocalDateTime date) {
        Optional<Meeting> optional = meetingRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Meeting meeting = new Meeting();
        meeting.setId(id);
        meeting.setTitle(title);
        meeting.setDate(date);
        return meetingRepository.save(meeting);
    }

    private Schedule createSchedule(Integer id, User user, String startTime, String endTime, DayOfWeek dayOfWeek) {
        Optional<Schedule> optional = scheduleRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Schedule schedule = new Schedule();
        schedule.setId(id);
        schedule.setUser(user);
        schedule.setStartTime(new java.sql.Time(LocalTime.parse(startTime).toNanoOfDay() / 1000000));
        schedule.setEndTime(new java.sql.Time(LocalTime.parse(endTime).toNanoOfDay() / 1000000));
        schedule.setDayOfWeek(dayOfWeek);
        return scheduleRepository.save(schedule);
    }

    private Message createMessage(Integer id, User sender, String subject, String text, LocalDateTime creationDate) {
        Optional<Message> optional = messageRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Message message = new Message();
        message.setId(id);
        message.setSubject(subject);
        message.setText(text);
        message.setSender(sender);
        message.setCreationDate(creationDate);
        return messageRepository.save(message);
    }

    private void addUserToMessage(Message message, User user) {
        if (!message.getRecipients().contains(user)) {
            message.getRecipients().add(user);
            messageRepository.save(message);
        }
    }

    private void inviteUserToMeeting(Meeting meeting, User user) {
        if (!meeting.getInvitees().contains(user)) {
            meeting.getInvitees().add(user);
            meetingRepository.save(meeting);
        }
    }

    private Pto createPto(Integer id, User user, LocalDateTime day, Pto.Type type) {
        Optional<Pto> optional = ptoRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Pto pto = new Pto();
        pto.setId(id);
        pto.setUser(user);
        pto.setDay(day.toLocalDate());
        pto.setType(type);
        return ptoRepository.save(pto);
    }

    private Purchase createPurchase(Integer id, Product product, User provider, Double amount, Double cost, LocalDateTime datetime) {
        Optional<Purchase> optional = purchaseRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Purchase purchase = new Purchase();
        purchase.setId(id);
        purchase.setProduct(product);
        purchase.setProvider(provider);
        purchase.setCost(cost);
        purchase.setAmount(amount);
        purchase.setDatetime(datetime);
        return purchaseRepository.save(purchase);
    }

    private Sale createSale(Integer id, Product product, User salesman, Double amount, Double price, LocalDateTime datetime) {
        Optional<Sale> optional = saleRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Sale sale = new Sale();
        sale.setId(id);
        sale.setProduct(product);
        sale.setSalesman(salesman);
        sale.setAmount(amount);
        sale.setPrice(price);
        sale.setDatetime(datetime);
        return saleRepository.save(sale);
    }

    private Work createWork(Integer id, User user, LocalDateTime startTime, LocalDateTime endTime) {
        Optional<Work> optional = workRepository.get(id);
        if (optional.isPresent())
            return optional.get();
        Work work = new Work();
        work.setId(id);
        work.setUser(user);
        work.setStartTime(startTime);
        work.setEndTime(endTime);
        return workRepository.save(work);
    }
}
