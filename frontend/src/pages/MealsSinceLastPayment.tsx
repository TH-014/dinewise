import { useEffect, useState } from "react";
import { format } from "date-fns";
import { Button } from "@/components/ui/button";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
// import { MealConfirmation } from "@/types"; // If you already have this

interface MealConfirmation {
  id: number;
  stdId: string;
  mealDate: string;
  willLunch: boolean;
  willDinner: boolean;
  createdAt: string;
  updatedAt: string;
}


const MealsSinceLastPayment = () => {
  const [meals, setMeals] = useState<MealConfirmation[]>([]);
  const navigate = useNavigate();
  const stdId = localStorage.getItem("studentId");

  useEffect(() => {
    const fetchMeals = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/student/meals/since-last-payment/${stdId}`, {
          method: 'GET',
          credentials: 'include'
        });

        if (response.ok) {
          const data = await response.json();
          setMeals(data);
        } else {
          toast.error("Failed to load meals.");
        }
      } catch (err) {
        toast.error("Network error.");
      }
    };

    fetchMeals();
  }, []);

  return (
    <div className="max-w-3xl mx-auto py-8 px-4">
      <h1 className="text-2xl font-bold mb-4">Meals After Last Payment</h1>
      <Button variant="outline" onClick={() => navigate('/dashboard')} className="mb-4">
        Back to Dashboard
      </Button>

      {meals.length === 0 ? (
        <p className="text-gray-600">No meals found after your last payment.</p>
      ) : (
        <ul className="space-y-3">
          {meals.map(meal => (
            <li key={meal.id} className="p-4 border rounded bg-white shadow">
              <p><strong>Date:</strong> {format(new Date(meal.mealDate), 'PPP')}</p>
              <p><strong>Lunch:</strong> {meal.willLunch ? '✅' : '❌'}, <strong>Dinner:</strong> {meal.willDinner ? '✅' : '❌'}</p>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default MealsSinceLastPayment;
