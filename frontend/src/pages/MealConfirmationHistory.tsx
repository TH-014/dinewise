import { useEffect, useState } from 'react';
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogFooter
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';
import { Label } from '@/components/ui/label';
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue
} from '@/components/ui/select';
import { useNavigate } from 'react-router-dom';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Switch } from '@/components/ui/switch';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Utensils, Calendar as CalendarIcon, User, LogOut, CheckCircle } from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '@/lib/utils';
import { Textarea } from '@/components/ui/textarea';
import { Star } from 'lucide-react';

// interface MealConfirmation {
//   id: number;
//   stdId: string;
//   mealDate: string;
//   willLunch: boolean;
//   willDinner: boolean;
//   createdAt: string;
//   updatedAt: string;
// }

interface MealConfirmation {
  id?: number;
  stdId: string;
  name: string; // <-- add this
}



const MealConfirmationHistory = () => {

    const navigate = useNavigate();
    const [confirmedMeals, setConfirmedMeals] = useState<MealConfirmation[]>([]);

    const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date());
    const [willLunch, setWillLunch] = useState(false);
    const [willDinner, setWillDinner] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [menu, setMenu] = useState<{ lunchItems: string[]; dinnerItems: string[] } | null>(null);
    const [menuDate, setMenuDate] = useState<Date>(new Date());
    const [commentText, setCommentText] = useState('');
    const [rating, setRating] = useState<number | null>(null);
    const [isRatingOpen, setIsRatingOpen] = useState(false);
    const [isCommentOpen, setIsCommentOpen] = useState(false);

    const fetchMealConfirmations = async () => {
      if (!selectedDate) {
        toast.error('Please select a date');
        return;
      }
      const selecteddate = format(selectedDate, "yyyy-MM-dd");
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/manager/showConfirmations`,
        {
          method: 'POST',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            selectedDate: selecteddate,
            willLunch,
            willDinner
          })
        });
        if (!response.ok) {
          throw new Error('Failed to fetch meal confirmations');
        }
        console.log(response);
        const data = await response.json();
        setConfirmedMeals(data);
      } catch (error) {
        toast.error(error.message);
      } finally {
        setIsLoading(false);
      }
    };

    return (
        
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-3">
              <div className="bg-blue-600 p-2 rounded-lg">
                <Utensils className="h-6 w-6 text-white" />
              </div>
              <h1 className="text-xl font-bold text-gray-900">Manager Dashboard</h1>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          {/* Meal Confirmation Card */}
          <div className="lg:col-span-2">
            <Card className="shadow-lg border-0 bg-white/70 backdrop-blur-sm">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <Utensils className="h-6 w-6 text-green-600" />
                  <span>Show Meal Confirmation</span>
                </CardTitle>
              </CardHeader>
              <CardContent className="space-y-6">
                {/* Date Picker */}
                <div className="space-y-2">
                  <Label>Select Date</Label>
                  <Popover>
                    <PopoverTrigger asChild>
                      <Button
                        variant="outline"
                        className={cn(
                          "w-full justify-start text-left font-normal h-12",
                          !selectedDate && "text-muted-foreground"
                        )}
                      >
                        <CalendarIcon className="mr-2 h-4 w-4" />
                        {selectedDate ? format(selectedDate, "PPP") : <span>Pick a date</span>}
                      </Button>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar
                        mode="single"
                        selected={selectedDate}
                        onSelect={setSelectedDate}
                        initialFocus
                        className={cn("p-3 pointer-events-auto")}
                        // disabled={(date) => date < new Date(new Date().setHours(0, 0, 0, 0))}
                      />
                    </PopoverContent>
                  </Popover>
                </div>

                {/* Meal Options */}
                <div className="grid md:grid-cols-2 gap-6">
                  <div className="flex items-center justify-between p-4 border rounded-lg bg-orange-50 border-orange-200">
                    <div className="flex items-center space-x-3">
                      <div className="bg-orange-500 p-2 rounded-lg">
                        <Utensils className="h-5 w-5 text-white" />
                      </div>
                      <div>
                        <Label htmlFor="lunch" className="text-base font-medium">Lunch</Label>
                        <p className="text-sm text-gray-600">12:00 PM - 2:00 PM</p>
                      </div>
                    </div>
                    <Switch
                      id="lunch"
                      checked={willLunch}
                      onCheckedChange={setWillLunch}
                    />
                  </div>

                  <div className="flex items-center justify-between p-4 border rounded-lg bg-purple-50 border-purple-200">
                    <div className="flex items-center space-x-3">
                      <div className="bg-purple-500 p-2 rounded-lg">
                        <Utensils className="h-5 w-5 text-white" />
                      </div>
                      <div>
                        <Label htmlFor="dinner" className="text-base font-medium">Dinner</Label>
                        <p className="text-sm text-gray-600">7:00 PM - 9:00 PM</p>
                      </div>
                    </div>
                    <Switch
                      id="dinner"
                      checked={willDinner}
                      onCheckedChange={setWillDinner}
                    />
                  </div>
                </div>

                {/* Confirmation Summary */}
                {/* {(willLunch || willDinner) && (
                  <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                    <div className="flex items-center space-x-2 mb-2">
                      <CheckCircle className="h-5 w-5 text-green-600" />
                      <span className="font-medium text-green-800">Confirmation Summary</span>
                    </div>
                    <p className="text-green-700">
                      You will have {willLunch && willDinner ? 'lunch and dinner' : willLunch ? 'lunch only' : 'dinner only'} on{' '}
                      {selectedDate ? format(selectedDate, "PPP") : 'the selected date'}.
                    </p>
                  </div>
                )} */}

                {/* Submit Button */}
                <Button 
                  onClick={fetchMealConfirmations}
                  disabled={isSubmitting || (!willLunch && !willDinner) || !selectedDate}
                  className="w-full h-12 text-lg bg-green-600 hover:bg-green-700"
                >
                  {isSubmitting ? 'Submitting...' : 'Show Meal Confirmations'}
                </Button>
              </CardContent>
            </Card>
            {confirmedMeals.length > 0 && (
            <div className="mt-6">
                <h2 className="text-lg font-semibold mb-4 text-gray-800">
                Confirmed Students
                </h2>
                <div className="overflow-auto rounded-lg shadow border border-gray-200">
                <table className="min-w-full bg-white text-sm text-left">
                    <thead className="bg-gray-100 text-gray-700">
                    <tr>
                        <th className="px-6 py-3 border-b">Student ID</th>
                        <th className="px-6 py-3 border-b">Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    {confirmedMeals.map((meal) => (
                        <tr key={meal.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 border-b">{meal.stdId}</td>
                        <td className="px-6 py-4 border-b">{meal.name}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                </div>
            </div>
            )}
        </div>
      </main>
    </div>
    );
};

export default MealConfirmationHistory;