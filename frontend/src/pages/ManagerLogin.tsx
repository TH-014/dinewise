// ✅ src/pages/ManagerLogin.tsx
import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Utensils, ArrowLeft } from 'lucide-react';
import { toast } from 'sonner';

const ManagerLogin = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState({
    stdId: '',
    password: ''
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/manager/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(formData)
      });

      const data = await response.json();
      if (response.ok) {
        console.log('Manager Token:', data.authToken);
        toast.success(data.message || 'Login successful!');
        localStorage.setItem('authToken', data.authToken);
        localStorage.setItem('stdId', formData.stdId);
        console.log("Logggind from manager login page");
        navigate('/manager/dashboard');
      } else {
        toast.error(data.message || 'Login failed.');
      }
    } catch (err) {
      console.error('Login error:', err);
      toast.error('Network error. Try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="mb-8">
          <Button variant="ghost" onClick={() => navigate('/')} className="mb-4 hover:bg-white/50">
            <ArrowLeft className="h-4 w-4 mr-2" /> Back to Home
          </Button>
          <div className="text-center">
            <div className="flex justify-center mb-4">
              <div className="bg-green-600 p-3 rounded-full">
                <Utensils className="h-8 w-8 text-white" />
              </div>
            </div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Mess Manager Login</h1>
            <p className="text-gray-600">Only active mess managers can log in</p>
          </div>
        </div>

        <Card className="shadow-xl border-0 bg-white/70 backdrop-blur-sm">
          <CardHeader>
            <CardTitle>Manager Login</CardTitle>
            <CardDescription>Enter your ID and password</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="stdId">Student ID</Label>
                <Input
                  id="stdId"
                  name="stdId"
                  value={formData.stdId}
                  onChange={handleInputChange}
                  required
                  placeholder="e.g., STD2025004"
                  className="h-12"
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  required
                  placeholder="Enter your password"
                  className="h-12"
                />
              </div>

              <Button type="submit" className="w-full bg-green-600 hover:bg-green-700 h-12 text-lg" disabled={isLoading}>
                {isLoading ? 'Signing In...' : 'Sign In'}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default ManagerLogin;
