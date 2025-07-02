
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Calendar } from '@/components/ui/calendar';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Utensils, Calendar as CalendarIcon, User, LogOut, CheckCircle } from 'lucide-react';
import { format } from 'date-fns';
import { toast } from 'sonner';
import { cn } from '@/lib/utils';
// import { console } from 'inspector';

interface Student {
  stdId: string;
  firstName: string;
  email: string;
  imageUrl?: string;
  phoneNumber?: string;
}

interface MealConfirmation {
  id: number;
  stdId: string;
  mealDate: string;
  willLunch: boolean;
  willDinner: boolean;
  createdAt: string;
  updatedAt: string;
}


const Dashboard = () => {
  const navigate = useNavigate();
  const [student, setStudent] = useState<Student | null>(null);
  // const [confirmedMeals, setConfirmedMeals] = useState([]);
  const [confirmedMeals, setConfirmedMeals] = useState<MealConfirmation[]>([]);

  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date());
  const [willLunch, setWillLunch] = useState(false);
  const [willDinner, setWillDinner] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [menu, setMenu] = useState<{ lunchItems: string[]; dinnerItems: string[] } | null>(null);
  const [menuDate, setMenuDate] = useState<Date>(new Date());


  useEffect(() => {
    // Check authentication and load user data
    const checkAuth = () => {
      // const cookies = document.cookie.split(';');
      // const authToken = cookies.find(cookie => cookie.trim().startsWith('authToken='));
      const authToken = localStorage.getItem('authToken'); 
      console.log('Auth Token in Dashboard:', authToken); // Debugging line to check auth token
      
      if (!authToken) {
        toast.error('Please login to access the dashboard.');
        navigate('/login');
        return;
      }

      //       localStorage.setItem('studentId', formData.studentId); // Store studentId in localStorage
      // localStorage.setItem('name', data.student.name);
      // localStorage.setItem('email', data.student.email);
      // localStorage.setItem('phoneNumber', data.student.phoneNumber);
      // localStorage.setItem('address', data.student.address);
      const studentId = localStorage.getItem('studentId');
      const name = localStorage.getItem('name');
      const email = localStorage.getItem('email');
      const phoneNumber = localStorage.getItem('phoneNumber');
      const address = localStorage.getItem('address');

      if (studentId) {
        fetchConfirmedMeals(studentId);
      }

      if (selectedDate) {
        fetchMenuForDate(selectedDate);
      }

  
      setStudent({
        stdId: studentId,
        firstName: name,
        email: email,
        imageUrl: '',
        phoneNumber: phoneNumber
      });
      setIsLoading(false);
    };

    checkAuth();
  }, [navigate]);

  useEffect(() => {
    if (selectedDate) {
      fetchMenuForDate(selectedDate);
    }
  }, [selectedDate]);


  useEffect(() => {
    if (menuDate) {
      fetchMenuForDate(menuDate);
    }
  }, [menuDate]);




    const fetchConfirmedMeals = async (stdId: string) => {
      try {
        const today = new Date().toISOString().split('T')[0]; // Format: YYYY-MM-DD
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/student/from/${today}`, {
          method: 'GET',
          credentials: 'include', // Include cookies (authToken)
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Fetched meals:', data);
          setConfirmedMeals(data);
        } else {
          console.error('Failed to fetch meals');
        }
      } catch (error) {
        console.error('Error fetching meals:', error);
      }
    };


//     const fetchMenuForDate = async (date: Date) => {
//   try {
//     const formattedDate = format(date, 'yyyy-MM-dd');
//     // console.log(formattedDate)
//     const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/menus?date=${formattedDate}`);
//     const data = await response.json();
//     // console.log(data);
//     if (data.lunchItems || data.dinnerItems) {
//       setMenu({ lunchItems: data.lunchItems || [], dinnerItems: data.dinnerItems || [] });
//     } else {
//       setMenu(null);
//     }
//   } catch (error) {
//     console.error("Error fetching menu:", error);
//     setMenu(null);
//   }
// };


const fetchMenuForDate = async (date: Date) => {
      try {
        const formattedDate = format(date, 'yyyy-MM-dd');
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/menus?date=${formattedDate}`, {
          method: 'GET',
          credentials: 'include', // Include cookies for authentication
        });
        const data = await response.json();

        if (data.lunchItems || data.dinnerItems) {
          setMenu({ lunchItems: data.lunchItems || [], dinnerItems: data.dinnerItems || [] });
        } else {
          setMenu(null);
        }
      } catch (error) {
        console.error("Error fetching menu:", error);
        setMenu(null);
      }
    };









  const handleMealSubmission = async () => {
    if (!selectedDate || !student) {
      toast.error('Please select a date and ensure you are logged in.');
      return;
    }

    setIsSubmitting(true);

    const mealData = {
      stdId: student.stdId,
      mealDate: format(selectedDate, 'yyyy-MM-dd'),
      willLunch,
      willDinner
    };

    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/student/mealconfirmation`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', // Include cookies for authentication
        body: JSON.stringify(mealData),
      });

      if (response.ok) {
        const result = await response.json();
        toast.success('Meal confirmation submitted successfully!');
        console.log('Meal confirmation response:', result);
        await fetchConfirmedMeals(student.stdId); // refresh data
      } else {
        toast.error('Failed to submit meal confirmation. Please try again.');
      }
    } catch (error) {
      console.error('Meal confirmation error:', error);
      toast.error('Network error. Please check your connection and try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  // const handleLogout = () => {
  //   // Clear the auth cookie
  //   document.cookie = 'authToken=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  //   toast.success('Logged out successfully.');
  //   navigate('/');
  // };


  const handleLogout = () => {
    // Clear the auth cookie
    document.cookie = 'authToken=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';

    // Clear localStorage items
    localStorage.removeItem('authToken');
    localStorage.removeItem('studentId');
    localStorage.removeItem('name');
    localStorage.removeItem('email');
    localStorage.removeItem('phoneNumber');
    localStorage.removeItem('address');
    localStorage.removeItem('role'); // If you're storing role (manager/student)

    toast.success('Logged out successfully.');
    navigate('/');
  };



  const handleDeleteMeal = async (mealId: number) => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/student/mealconfirmation/${mealId}`, {
        method: 'DELETE',
        credentials: 'include',
      });

      if (response.ok) {
        toast.success("Meal confirmation deleted.");
        if (student?.stdId) {
          await fetchConfirmedMeals(student.stdId); // refresh list
        }
      } else {
        toast.error("Failed to delete meal.");
      }
    } catch (error) {
      console.error("Delete meal error:", error);
      toast.error("An error occurred while deleting.");
    }
  };


  if (isLoading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  if (!student) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50 flex items-center justify-center">
        <div className="text-center">
          <p className="text-gray-600">Unable to load student information.</p>
          <Button onClick={() => navigate('/login')} className="mt-4">
            Go to Login
          </Button>
        </div>
      </div>
    );
  }

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
              <h1 className="text-xl font-bold text-gray-900">Student Dashboard</h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-gray-600">Welcome, {student.firstName}!</span>
              <Button 
                variant="outline" 
                onClick={handleLogout}
                className="flex items-center space-x-2"
              >
                <LogOut className="h-4 w-4" />
                <span>Logout</span>
              </Button>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="grid lg:grid-cols-3 gap-8">
          {/* Profile Card */}
          <div className="lg:col-span-1">
            <Card className="shadow-lg border-0 bg-white/70 backdrop-blur-sm">
              <CardHeader className="text-center">
                <div className="flex justify-center mb-4">
                  <Avatar className="h-20 w-20">
                    <AvatarImage src={student.imageUrl} alt={`${student.firstName}`} />
                    <AvatarFallback className="bg-blue-100 text-blue-600 text-xl">
                      {student.firstName[0]}
                    </AvatarFallback>
                  </Avatar>
                </div>
                <CardTitle className="text-xl">{student.firstName}</CardTitle>
                <CardDescription className="text-lg font-medium text-blue-600">
                  {student.stdId}
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex items-center space-x-3 text-gray-600">
                  <User className="h-4 w-4" />
                  <span className="text-sm">{student.email}</span>
                </div>
                {student.phoneNumber && (
                  <div className="flex items-center space-x-3 text-gray-600">
                    <span className="text-sm">{student.phoneNumber}</span>
                  </div>
                )}
              </CardContent>
            </Card>

            <Card className="mt-8 shadow-lg border-0 bg-white/70 backdrop-blur-sm">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <CalendarIcon className="h-6 w-6 text-blue-600" />
                  <span>Your Upcoming Meal Confirmations</span>
                </CardTitle>
                <CardDescription>All confirmed meals from today onward</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                {confirmedMeals.length === 0 ? (
                  <p className="text-gray-600">No meals confirmed yet.</p>
                ) : (
                  confirmedMeals.map((meal) => (
                    <div key={meal.id} className="border p-4 rounded-md bg-gray-50 flex justify-between items-center">
                      <div>
                        <h4 className="font-semibold">{format(new Date(meal.mealDate), 'PPP')}</h4>
                        <p className="text-sm text-gray-600">
                          Lunch: {meal.willLunch ? '✅ Yes' : '❌ No'}, Dinner: {meal.willDinner ? '✅ Yes' : '❌ No'}
                        </p>
                      </div>

                      <div className="flex items-center space-x-2">
                        <CheckCircle className="text-green-500 h-5 w-5" />
                        {new Date(meal.mealDate) >= new Date() && (
                          <Button
                            variant="destructive"
                            size="sm"
                            onClick={() => handleDeleteMeal(meal.id)}
                          >
                            Delete
                          </Button>
                        )}
                      </div>
                    </div>
                    // <div key={meal.id} className="border p-4 rounded-md bg-gray-50 flex justify-between items-center">
                    //   <div>
                    //     <h4 className="font-semibold">{format(new Date(meal.mealDate), 'PPP')}</h4>
                    //     <p className="text-sm text-gray-600">
                    //       Lunch: {meal.willLunch ? '✅ Yes' : '❌ No'}, Dinner: {meal.willDinner ? '✅ Yes' : '❌ No'}
                    //     </p>
                    //   </div>
                    //   <CheckCircle className="text-green-500 h-5 w-5" />
                    // </div>
                  ))
                )}
              </CardContent>
            </Card>

          </div>

          {/* Meal Confirmation Card */}
          <div className="lg:col-span-2">
            <Card className="shadow-lg border-0 bg-white/70 backdrop-blur-sm">
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <Utensils className="h-6 w-6 text-green-600" />
                  <span>Meal Confirmation</span>
                </CardTitle>
                <CardDescription>
                  Select your meal preferences for the chosen date
                </CardDescription>
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
                        disabled={(date) => date < new Date(new Date().setHours(0, 0, 0, 0))}
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
                {(willLunch || willDinner) && (
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
                )}

                {/* Submit Button */}
                <Button 
                  onClick={handleMealSubmission}
                  disabled={isSubmitting || (!willLunch && !willDinner) || !selectedDate}
                  className="w-full h-12 text-lg bg-green-600 hover:bg-green-700"
                >
                  {isSubmitting ? 'Submitting...' : 'Confirm Meal Preferences'}
                </Button>
              </CardContent>
            </Card>
          </div>
          {/* {menu && (
              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <div className="mb-2">
                  <h4 className="text-blue-800 font-semibold">Menu for {format(selectedDate!, 'PPP')}</h4>
                </div>
                <div>
                  <strong>Lunch:</strong> {menu.lunchItems.length ? menu.lunchItems.join(', ') : 'Not set'}
                </div>
                <div>
                  <strong>Dinner:</strong> {menu.dinnerItems.length ? menu.dinnerItems.join(', ') : 'Not set'}
                </div>
              </div>
            )} */}

             <Card className="mt-8 shadow-lg border-0 bg-white/70 backdrop-blur-sm">
                <CardHeader>
                  <CardTitle className="flex items-center space-x-2">
                    <CalendarIcon className="h-6 w-6 text-blue-600" />
                    <span>View Menu</span>
                  </CardTitle>
                  <CardDescription>
                    Select a date to see the menu (defaults to today)
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="space-y-2">
                    <Label>Select Date</Label>
                    <Popover>
                      <PopoverTrigger asChild>
                        <Button
                          variant="outline"
                          className={cn(
                            "w-full justify-start text-left font-normal h-12",
                            !menuDate && "text-muted-foreground"
                          )}
                        >
                          <CalendarIcon className="mr-2 h-4 w-4" />
                          {menuDate ? format(menuDate, "PPP") : <span>Pick a date</span>}
                        </Button>
                      </PopoverTrigger>
                      <PopoverContent className="w-auto p-0" align="start">
                        <Calendar
                          mode="single"
                          selected={menuDate}
                          onSelect={setMenuDate}
                          initialFocus
                          className="p-3 pointer-events-auto"
                        />
                      </PopoverContent>
                    </Popover>
                  </div>

                  {menu ? (
                    <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                      <div className="mb-2">
                        <h4 className="text-blue-800 font-semibold">Menu for {format(menuDate, 'PPP')}</h4>
                      </div>
                      <div>
                        <strong>Lunch:</strong> {menu.lunchItems.length ? menu.lunchItems.join(', ') : 'Not set'}
                      </div>
                      <div>
                        <strong>Dinner:</strong> {menu.dinnerItems.length ? menu.dinnerItems.join(', ') : 'Not set'}
                      </div>
                    </div>
                  ) : (
                    <p className="text-gray-600">No menu available for this date.</p>
                  )}
                </CardContent>
              </Card>

        </div>
      </main>
    </div>
  );
};

export default Dashboard;
