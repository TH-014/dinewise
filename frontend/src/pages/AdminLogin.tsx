// src/pages/AdminLogin.tsx
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { ShieldCheck, ArrowLeft } from 'lucide-react';
import { toast } from 'sonner';

const AdminLogin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/admin/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(formData)
      });

      const data = await res.json();
      if (res.ok) {
        toast.success(data.message);
        localStorage.setItem('adminToken', data.authToken);
        navigate('/admin/dashboard');
      } else {
        toast.error(data.message || 'Login failed');
      }
    } catch (err) {
      toast.error('Login error. Try again.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-yellow-50 via-white to-green-50 p-4">
      <div className="w-full max-w-md">
        <Button variant="ghost" onClick={() => navigate('/')} className="mb-4">
          <ArrowLeft className="mr-2 h-4 w-4" /> Back to Home
        </Button>

        <div className="text-center">
          <div className="flex justify-center mb-4">
            <div className="bg-yellow-600 p-3 rounded-full">
              <ShieldCheck className="h-8 w-8 text-white" />
            </div>
          </div>
          <h1 className="text-3xl font-bold mb-2">Admin Login</h1>
          <p className="text-gray-600">Login to manage mess manager applications</p>
        </div>

        <Card className="mt-4 shadow-xl border-0 bg-white/70 backdrop-blur-sm">
          <CardHeader>
            <CardTitle>Admin Login</CardTitle>
            <CardDescription>Enter your credentials</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className="space-y-6">
              <div className="space-y-2">
                <Label htmlFor="username">Username</Label>
                <Input name="username" value={formData.username} onChange={handleChange} required />
              </div>
              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input type="password" name="password" value={formData.password} onChange={handleChange} required />
              </div>
              <Button type="submit" className="w-full h-12 bg-yellow-600 hover:bg-yellow-700 text-lg" disabled={isLoading}>
                {isLoading ? 'Logging in...' : 'Log In'}
              </Button>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default AdminLogin;
