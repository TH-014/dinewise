package com.example.dinewise.controller;
import java.time.LocalDate;
import java.util.List;
import com.example.dinewise.model.Menu;
import com.example.dinewise.model.Stock;

public class AiPromptBuilder {
    public static String buildPrompt(List<Menu> menus, List<Stock> stocks, long lunchCount, long dinnerCount,
                                     String mode, boolean allowShopping) {

        StringBuilder sb = new StringBuilder();

        sb.append("You are a mess meal planner AI assistant.\n");
        sb.append("The manager is asking for a menu suggestion for ").append(LocalDate.now()).append("\n\n");

        sb.append("Manager intent(Type of meal let say if normal then general food, if fest then special food, you can analyse them from the menus data): ").append(mode).append("\n");
        sb.append("Allow shopping for items(Will mess manager go for shopping today or not, if not possible for the specified number of students without shopping then notify it in your response): ").append(allowShopping ? "Yes" : "No").append("\n\n");

        sb.append("Stock availability:\n");
        for (Stock s : stocks) {
            sb.append("- ").append(s.getItemName())
              .append(": ").append(s.getQuantity()).append(" ").append(s.getUnit())
              .append(" @ ").append(s.getPerUnitPrice()).append(" per unit\n");
        }

        sb.append("\nLast few days menus(Recent Menus):\n");
        for (Menu m : menus) {
            sb.append(m.getMenuDate()).append(":\n");
            sb.append("Lunch: ").append(String.join(", ", m.getLunchItems())).append("\n");
            sb.append("Lunch Type: ").append(m.getLunchType()).append("\n");
            sb.append("Dinner: ").append(String.join(", ", m.getDinnerItems())).append("\n");
            sb.append("Dinner Type: ").append(m.getDinnerType()).append("\n");
        }

        sb.append("\nStudent Count(How many student will have lunch or dinner):\n");
        sb.append("Lunch: ").append(lunchCount).append("\n");
        sb.append("Dinner: ").append(dinnerCount).append("\n");

        sb.append("\nPlease suggest a lunch and dinner menu for the given conditions. Consider the data given already, also consider the season running now, and most importantly the location(Karwan Bazar, Dhaka, Bangladesh) and availability of the products that you are suggesting. And also consider the budget issue if it is normal meal then the budget will be 50-60 tk, improvement budget will be 80-100 tk, fest budget will be 150-200 tk. Most importantly when there is fest in one meal other should be by default normal food.\n");
        sb.append("Your response should strictly follow the following format:(First one is success case(if menu suggestion is possible with specified data), second one is failure case(menu suggestion is not possible e.g. if mess manager is not willing to go for shopping but there is no stock available))\n");
        sb.append(" {\r\n" + //
                        "  \"status\": \"success\"\r\n" + //
                        "  \"option1\": {\r\n" + //
                        "    \"Lunch\": [\"Rice\", \"Chicken Curry\", \"Salad\"],\r\n" + //
                        "    \"Dinner\": [\"Paratha\", \"Beef Bhuna\", \"Mixed Vegetables\"]\r\n" + //
                        "  },\r\n" + //
                        "  \"option2\": {\r\n" + //
                        "    \"Lunch\": [\"Khichuri\", \"Egg Curry\", \"Fried Brinjal\"],\r\n" + //
                        "    \"Dinner\": [\"Rice\", \"Fish Curry\", \"Daal\"]\r\n" + //
                        "  },\r\n" + //
                        "  \"option3\": {\r\n" + //
                        "    \"Lunch\": [\"Polao\", \"Chicken Roast\", \"Raita\"],\r\n" + //
                        "    \"Dinner\": [\"Noodles\", \"Shrimp Curry\", \"Cucumber Salad\"]\r\n" + //
                        "  }\r\n" + //
                        "}\r\n" + //
                        "");
        sb.append("{\r\n" + //
                        "  \"status\": \"failure\",\r\n" + //
                        "  \"option1\": {\r\n" + //
                        "    \"Lunch\": [],\r\n" + //
                        "    \"Dinner\": []\r\n" + //
                        "  },\r\n" + //
                        "  \"option2\": {\r\n" + //
                        "    \"Lunch\": [],\r\n" + //
                        "    \"Dinner\": []\r\n" + //
                        "  },\r\n" + //
                        "  \"option3\": {\r\n" + //
                        "    \"Lunch\": [],\r\n" + //
                        "    \"Dinner\": []\r\n" + //
                        "  }\r\n" + //
                        "}\r\n" + //
                        "\n" + //
                        "" );
                    
        return sb.toString();
    }
}
