
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Utensils, GraduationCap, Clock, Users } from 'lucide-react';
import { Button } from '@/components/ui/button';

const Index = () => {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // Check if user is authenticated by looking for auth token
    const checkAuth = () => {
      const cookies = document.cookie.split(';');
      const authToken = cookies.find(cookie => cookie.trim().startsWith('authToken='));
      setIsAuthenticated(!!authToken);
    };
    
    checkAuth();
  }, []);

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
              <h1 className="text-xl font-bold text-gray-900">DineWise Portal</h1>
            </div>
            <div className="flex space-x-3">
              {isAuthenticated ? (
                <Button onClick={() => navigate('/dashboard')} className="bg-blue-600 hover:bg-blue-700">
                  Go to Dashboard
                </Button>
              ) : (
                <>
                  <Button variant="outline" onClick={() => navigate('/login')}>
                    Login
                  </Button>
                  <Button onClick={() => navigate('/signup/request')} className="bg-blue-600 hover:bg-blue-700">
                    Sign Up
                  </Button>
                </>
              )}
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="text-center">
          <div className="flex justify-center mb-8">
            <div className="bg-gradient-to-r from-blue-600 to-green-600 p-6 rounded-full">
              <GraduationCap className="h-16 w-16 text-white" />
            </div>
          </div>
          
          <h1 className="text-4xl md:text-6xl font-bold text-gray-900 mb-6">
            <span className="bg-gradient-to-r from-blue-600 to-green-600 bg-clip-text text-transparent"> DineWise </span>
          </h1>
          
          <p className="text-xl text-gray-600 mb-12 max-w-3xl mx-auto">
            Easily manage your daily meal preferences with our intuitive platform. 
            Plan your lunch and dinner in advance for a seamless dining experience.
          </p>

          <div className="flex flex-col sm:flex-row gap-4 justify-center mb-16">
            {!isAuthenticated ? (
              <>
                <Button 
                  size="lg" 
                  onClick={() => navigate('/signup/request')}
                  className="bg-blue-600 hover:bg-blue-700 text-lg px-8 py-3"
                >
                  Get Started
                </Button>
                {/* <Button 
                  size="lg" 
                  variant="outline" 
                  onClick={() => navigate('/login')}
                  className="text-lg px-8 py-3"
                >
                  Already have an account?
                </Button> */}

                <Button variant="outline" onClick={() => navigate('/login')}>
                  Student Login
                </Button>
                <Button variant="outline" onClick={() => navigate('/manager-login')}>
                  Mess Manager Login
                </Button>
                <Button onClick={() => navigate('/signup/request')} className="bg-blue-600 hover:bg-blue-700">
                  Sign Up
                </Button>

              </>
            ) : (
              <Button 
                size="lg" 
                onClick={() => navigate('/dashboard')}
                className="bg-green-600 hover:bg-green-700 text-lg px-8 py-3"
              >
                Open Dashboard
              </Button>
            )}
          </div>
        </div>

        {/* Features */}
        <div className="grid md:grid-cols-3 gap-8 mt-20">
          <div className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div className="bg-blue-100 w-12 h-12 rounded-lg flex items-center justify-center mb-6">
              {/* <Clock className="h-6 w-6 text-blue-600" /> */}
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Easy Payment</h3>
            <p className="text-gray-600">
              {/* Plan your meals in advance with our intuitive date picker and confirmation system. */}
            </p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div className="bg-green-100 w-12 h-12 rounded-lg flex items-center justify-center mb-6">
              {/* <Utensils className="h-6 w-6 text-green-600" /> */}
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-4">My Dues</h3>
            <p className="text-gray-600">
              {/* Choose between lunch and dinner options for each day with flexible preferences. */}
            </p>
          </div>

          <div className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-shadow duration-300">
            <div className="bg-purple-100 w-12 h-12 rounded-lg flex items-center justify-center mb-6">
              {/* <Users className="h-6 w-6 text-purple-600" /> */}
            </div>
            <h3 className="text-xl font-semibold text-gray-900 mb-4">...</h3>
            <p className="text-gray-600">
              {/* Your meal preferences are protected with secure authentication and token-based access. */}
            </p>
          </div>

        </div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-50 border-t mt-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <div className="text-center text-gray-600">
            <p>&copy; 2025 DineWise Portal. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default Index;
