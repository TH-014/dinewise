// ManagerDashboard.tsx
import { useEffect, useState } from 'react';
import { toast } from 'sonner';
import { Card, CardContent, CardTitle } from '@/components/ui/card';
import { Button } from  '@/components/ui/button';
import { useNavigate } from 'react-router-dom';

const ManagerDashboard = () => {
  const navigate = useNavigate();
  const [lunchCount, setLunchCount] = useState<number | null>(null);
  const [dinnerCount, setDinnerCount] = useState<number | null>(null);

  useEffect(() => {
    const fetchMealStats = async () => {
      try {
        const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/manager/dashboard/stats`, {
          method: 'GET',
          credentials: 'include',
        });

        if (!response.ok) {
          throw new Error('Failed to fetch dashboard stats');
        }

        const data = await response.json();
        setLunchCount(data.lunchCount);
        setDinnerCount(data.dinnerCount);
      } catch (err) {
        toast.error((err as Error).message);
      }
    };

    fetchMealStats();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 py-10 px-6">
      <h1 className="text-3xl font-bold text-center mb-10">Mess Manager Dashboard</h1>
      <div className="grid md:grid-cols-2 gap-8 max-w-4xl mx-auto">
        <Card className="shadow-lg">
          <CardContent className="p-6">
            <CardTitle>Lunch Confirmations (Tomorrow)</CardTitle>
            <p className="text-4xl font-bold text-green-600 mt-4">{lunchCount ?? 'Loading...'}</p>
          </CardContent>
        </Card>
        <Card className="shadow-lg">
          <CardContent className="p-6">
            <CardTitle>Dinner Confirmations (Tomorrow)</CardTitle>
            <p className="text-4xl font-bold text-blue-600 mt-4">{dinnerCount ?? 'Loading...'}</p>
          </CardContent>
        </Card>
        <Button
          onClick={() => navigate('/stocks')}
          className="bg-green-600 hover:bg-green-700"
        >
          View Stock Storage
        </Button>

        <Button 
          className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md"
          onClick={() => navigate('/manager/menu')}
        >
          Set or Update Menu
        </Button>

      </div>
    </div>
  );
};

export default ManagerDashboard;