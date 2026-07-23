package com.notsodaft.config;

import com.notsodaft.model.Review;
import com.notsodaft.model.User;
import com.notsodaft.repository.ReviewRepository;
import com.notsodaft.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
public class DataSeeder{
    @Bean
    public CommandLineRunner seedData(ReviewRepository reviewRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if (reviewRepository.count() >= 10) return; 

            User testUser;
            if (!userRepository.existsByEmail("test@notsodaft.com")){
                testUser = new User();
                testUser.setName("Test User");
                testUser.setEmail("test@notsodaft.com");
                testUser.setPassword(passwordEncoder.encode("password123"));
                testUser.setRole(User.Role.TENANT);
                userRepository.save(testUser);
            } else {
                testUser = userRepository.findByEmail("test@notsodaft.com").get();
            }

            String[][] properties = {
                {"14 Grafton Street", "D02 VY24", "Dublin", "53.3418", "-6.2596"},
                {"42 O'Connell Street", "D01 T6K8", "Dublin", "53.3498", "-6.2603"},
                {"8 Patrick Street", "T12 AB34", "Cork", "51.8985", "-8.4756"},
                {"22 Shop Street", "H91 CD56", "Galway", "53.2707", "-9.0568"},
                {"5 O'Connell Street", "V94 EF78", "Limerick", "52.6638", "-8.6267"},
                {"3 Quay Street", "X91 GH90", "Waterford", "52.2593", "-7.1101"},
                {"77 Rathmines Road", "D06 IJ11", "Dublin", "53.3205", "-6.2684"},
                {"12 High Street", "R95 KL22", "Kilkenny", "52.6541", "-7.2448"},
                {"9 Wine Street", "F91 MN33", "Sligo", "54.2697", "-8.4694"},
                {"15 Main Street", "Y35 OP44", "Wexford", "52.3368", "-6.4633"},
                {"7 College Road", "H91 QR55", "Galway", "53.2743", "-9.0514"},
                {"33 South Mall", "T12 ST66", "Cork", "51.8979", "-8.4731"},
                {"5 Cathedral Place", "V94 UV77", "Limerick", "52.6680", "-8.6231"},
                {"18 Georges Street", "D02 WX88", "Dublin", "53.3403", "-6.2631"},
                {"24 Dawson Street", "D02 YZ99", "Dublin", "53.3401", "-6.2585"},
                {"6 Castle Street", "D02 AA11", "Dublin", "53.3447", "-6.2675"},
                {"11 Pearse Street", "D02 BB22", "Dublin", "53.3459", "-6.2503"},
                {"9 Thomas Street", "D08 CC33", "Dublin", "53.3416", "-6.2803"},
                {"3 Francis Street", "D08 DD44", "Dublin", "53.3389", "-6.2820"},
                {"45 Camden Street", "D02 EE55", "Dublin", "53.3319", "-6.2625"},
                {"67 Baggot Street", "D04 FF66", "Dublin", "53.3327", "-6.2477"},
                {"12 Merrion Square", "D02 GG77", "Dublin", "53.3381", "-6.2487"},
                {"8 Fitzwilliam Square", "D02 HH88", "Dublin", "53.3356", "-6.2506"},
                {"20 Harcourt Street", "D02 II99", "Dublin", "53.3329", "-6.2622"},
                {"5 Wicklow Street", "D02 JJ11", "Dublin", "53.3426", "-6.2598"},
                {"30 Westmoreland Street", "D02 KK22", "Dublin", "53.3453", "-6.2581"},
                {"14 Fleet Street", "D02 LL33", "Dublin", "53.3456", "-6.2564"},
                {"7 Temple Bar", "D02 MM44", "Dublin", "53.3454", "-6.2646"},
                {"3 Crown Alley", "D02 NN55", "Dublin", "53.3452", "-6.2648"},
                {"19 Wellington Quay", "D02 OO66", "Dublin", "53.3463", "-6.2660"},
                {"8 Bachelors Walk", "D01 PP77", "Dublin", "53.3478", "-6.2617"},
                {"22 Parnell Street", "D01 QQ88", "Dublin", "53.3512", "-6.2636"},
                {"15 Capel Street", "D01 RR99", "Dublin", "53.3497", "-6.2686"},
                {"6 Mary Street", "D01 SS11", "Dublin", "53.3491", "-6.2664"},
                {"11 Henry Street", "D01 TT22", "Dublin", "53.3489", "-6.2627"},
                {"4 Moore Street", "D01 UU33", "Dublin", "53.3499", "-6.2617"},
                {"9 Talbot Street", "D01 VV44", "Dublin", "53.3509", "-6.2576"},
                {"25 Gardiner Street", "D01 WW55", "Dublin", "53.3533", "-6.2558"},
                {"17 Mountjoy Square", "D01 XX66", "Dublin", "53.3563", "-6.2565"},
                {"8 Eccles Street", "D07 YY77", "Dublin", "53.3582", "-6.2670"},
                {"14 Dorset Street", "D01 ZZ88", "Dublin", "53.3601", "-6.2647"},
                {"6 Drumcondra Road", "D09 AA99", "Dublin", "53.3668", "-6.2577"},
                {"22 Clontarf Road", "D03 BB11", "Dublin", "53.3631", "-6.2228"},
                {"5 Sandymount Avenue", "D04 CC22", "Dublin", "53.3318", "-6.2225"},
                {"18 Ballsbridge Terrace", "D04 DD33", "Dublin", "53.3284", "-6.2370"},
                {"9 Ranelagh Road", "D06 EE44", "Dublin", "53.3229", "-6.2589"},
                {"33 Rathgar Avenue", "D06 FF55", "Dublin", "53.3169", "-6.2677"},
                {"7 Terenure Road", "D6W GG66", "Dublin", "53.3102", "-6.2829"},
                {"15 Crumlin Road", "D12 HH77", "Dublin", "53.3263", "-6.3011"},
                {"4 Walkinstown Avenue", "D12 II88", "Dublin", "53.3195", "-6.3294"},
                {"20 Bluebell Avenue", "D12 JJ99", "Dublin", "53.3270", "-6.3337"},
                {"8 Inchicore Road", "D08 KK11", "Dublin", "53.3384", "-6.3178"},
                {"12 Kilmainham Lane", "D08 LL22", "Dublin", "53.3415", "-6.3099"},
                {"5 Islandbridge", "D08 MM33", "Dublin", "53.3466", "-6.3100"},
                {"19 Chapelizod Road", "D20 NN44", "Dublin", "53.3503", "-6.3367"},
                {"7 Lucan Road", "D20 OO55", "Dublin", "53.3558", "-6.3650"},
                {"14 Tallaght Road", "D24 PP66", "Dublin", "53.2882", "-6.3731"},
                {"6 Firhouse Road", "D24 QQ77", "Dublin", "53.2771", "-6.3337"},
                {"22 Rathfarnham Road", "D14 RR88", "Dublin", "53.2989", "-6.2867"},
                {"9 Dundrum Road", "D14 SS99", "Dublin", "53.2937", "-6.2475"},
                {"3 Stillorgan Road", "D04 TT11", "Dublin", "53.3003", "-6.2196"},
                {"17 Foxrock Avenue", "D18 UU22", "Dublin", "53.2778", "-6.1820"},
                {"8 Dun Laoghaire Pier", "A96 VV33", "Dublin", "53.2944", "-6.1358"},
                {"15 Blackrock Main St", "A94 WW44", "Dublin", "53.3012", "-6.1767"},
                {"6 Monkstown Road", "A94 XX55", "Dublin", "53.3013", "-6.1489"},
                {"22 Dalkey Avenue", "A96 YY66", "Dublin", "53.2763", "-6.1025"},
                {"4 Killiney Hill Road", "A96 ZZ77", "Dublin", "53.2651", "-6.1078"},
                {"11 Bray Main Street", "A98 AA11", "Wicklow", "53.2009", "-6.1119"},
                {"7 Greystones Road", "A63 BB22", "Wicklow", "53.1445", "-6.0646"},
                {"18 Naas Road", "W91 CC33", "Kildare", "53.2193", "-6.6673"},
                {"5 Newbridge Main St", "W12 DD44", "Kildare", "53.1822", "-6.7948"},
                {"14 Maynooth Road", "W23 EE55", "Kildare", "53.3816", "-6.5931"},
                {"9 Celbridge Road", "W23 FF66", "Kildare", "53.3400", "-6.5437"},
                {"3 Drogheda Street", "A92 GG77", "Louth", "53.7194", "-6.3519"},
                {"20 Dundalk Main St", "A91 HH88", "Louth", "54.0029", "-6.4050"},
                {"8 Navan Road", "C15 II99", "Meath", "53.6521", "-6.6868"},
                {"15 Trim Street", "C15 JJ11", "Meath", "53.5544", "-6.7892"},
                {"6 Mullingar Street", "N91 KK22", "Westmeath", "53.5259", "-7.3384"},
                {"22 Athlone Main St", "N37 LL33", "Westmeath", "53.4237", "-7.9401"}
            };

            String[] reviewTexts = {
                "Great location, close to everything. The heating works well but there was some dampness in the bathroom.",
                "Landlord was responsive and maintenance issues were fixed quickly. Would recommend.",
                "Decent apartment for the price. A bit cold in winter but manageable with extra heating.",
                "The property was well maintained when we moved in. Neighbours are quiet and respectful.",
                "Had some issues with dampness in the first winter but landlord sorted it out promptly.",
                "Excellent value for money. Modern appliances and good insulation throughout.",
                "Location is perfect for commuting. The building management is professional and responsive.",
                "Lived here for two years. Minor maintenance issues but always resolved within 48 hours.",
                "Spacious rooms and good natural light. Heating system is efficient and cost-effective.",
                "Some wear and tear visible but overall a solid rental. Fair price for the area."
            };

            Review.PropertyType[] types = Review.PropertyType.values();
            Random random = new Random();

            for (int i = 0; i < properties.length; i++){
                String[] p = properties[i];
                Review review = new Review();
                review.setAddress(p[0]);
                review.setEircode(p[1]);
                review.setCounty(p[2]);
                review.setLat(Double.parseDouble(p[3]));
                review.setLng(Double.parseDouble(p[4]));
                review.setReviewText(reviewTexts[i % reviewTexts.length]);
                review.setDampnessScore(random.nextInt(4) + 2);
                review.setHeatingScore(random.nextInt(4) + 2);
                review.setMaintenanceScore(random.nextInt(4) + 2);
                review.setOverallScore(random.nextInt(4) + 2);
                review.setPropertyType(types[random.nextInt(types.length)]);
                review.setAuthor(testUser);
                review.setStatus(Review.ReviewStatus.APPROVED);
                review.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
                reviewRepository.save(review);
            }

            System.out.println("✅ Seeded " + properties.length + " reviews");
        };
    }
}