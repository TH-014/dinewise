import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Label } from '@/components/ui/label';

const SignupWithOTP = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    stdId: '',
    firstName: '',
    lastName: '',
    phoneNumber: '',
    imageUrl: '',
    presentAddress: '',
    permanentAddress: '',
  });

  const [otp, setOtp] = useState('');
  const [showOtpField, setShowOtpField] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSignupRequest = async () => {
    setLoading(true);
    console.log("Attempting signup request with data:", formData); // Add this
    try {
      const response = await fetch('http://localhost:8080/signup/request', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });
      console.log("Signup request response received:", response); // Add this
      const data = await response.json();
      console.log("Signup request response data:", data); // Add this
      if (response.ok) {
        toast.success(data.message);
        setShowOtpField(true);
      } else {
        toast.error(data.message);
      }
    } catch (err) {
      toast.error('Request failed');
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async () => {
    setLoading(true);
    try {
      const response = await fetch('http://localhost:8080/signup/verify', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: formData.email, otp }),
      });
      const data = await response.json();
      if (response.ok) {
        toast.success(data.message);
        navigate('/login');
      } else {
        toast.error(data.message);
      }
    } catch (err) {
      toast.error('Verification failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 via-white to-green-100 p-4">
      <Card className="w-full max-w-md shadow-lg border-0 bg-white/80 backdrop-blur-sm">
        <CardHeader>
          <CardTitle>Student Signup</CardTitle>
          <CardDescription>Register to Dinewise and verify via OTP</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          {!showOtpField && (
            <>
              {Object.entries(formData).map(([key, val]) => (
                <div key={key}>
                  <Label htmlFor={key} className="capitalize">{key.replace(/([A-Z])/g, ' $1')}</Label>
                  <Input
                    id={key}
                    name={key}
                    type={key === 'password' ? 'password' : 'text'}
                    value={val}
                    onChange={handleChange}
                    placeholder={`Enter ${key}`}
                  />
                </div>
              ))}
              <Button onClick={handleSignupRequest} className="w-full" disabled={loading}>
                {loading ? 'Sending OTP...' : 'Submit Request'}
              </Button>
            </>
          )}

          {showOtpField && (
            <div className="space-y-4 animate-fade-in">
              <div>
                <Label htmlFor="otp">Enter OTP sent to email</Label>
                <Input
                  id="otp"
                  name="otp"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  placeholder="6-digit OTP"
                />
              </div>
              <Button onClick={handleVerifyOtp} className="w-full" disabled={loading}>
                {loading ? 'Verifying...' : 'Verify OTP'}
              </Button>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default SignupWithOTP;
