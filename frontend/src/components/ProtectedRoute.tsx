
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

  useEffect(() => {
    const checkAuth = () => {
      // console.log('Checking authentication...'); // Debugging line to check auth flow
      // console.log(document.cookie); // Debugging line to check cookies
      // const cookies = document.cookie.split(';');
      // // console.log('Cookies:', cookies); // Debugging line to check cookies
      // const authToken = cookies.find(cookie => cookie.trim().startsWith('authToken='));
      const authToken = localStorage.getItem('authToken'); // Assuming authToken is stored in localStorage

      if (!authToken) {
        setIsAuthenticated(false);
        toast.error('Please login to access this page.');
        navigate('/login');
      } else {
        setIsAuthenticated(true);
      }
    };

    checkAuth();
  }, [navigate]);

  if (isAuthenticated === null) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-green-50 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Checking authentication...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return null;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
