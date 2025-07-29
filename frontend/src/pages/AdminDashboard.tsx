// src/pages/AdminDashboard.tsx
import { useState } from 'react';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';
import { format, parseISO } from 'date-fns';

const AdminDashboard = () => {
  const [applications, setApplications] = useState([]);

  const fetchApplications = async () => {
    try {
      const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/admin/applications`, {
        method: 'GET',
        credentials: 'include'
      });

      const data = await res.json();
      if (res.ok) {
        setApplications(data);
      } else {
        console.error(data.message);
      }
    } catch (err) {
      console.error('Fetch error:', err);
    }
  };

//   // Inside the fetchApplications() function
// const fetchApplications = async () => {
//   try {
//     const res = await fetch('http://localhost:8080/admin/applications', {
//       credentials: 'include',
//     });
//     const data = await res.json();
//     setApplications(data);
//   } catch (err) {
//     toast.error('Failed to fetch applications');
//   }
// };

// Approve handler
const handleApprove = async (id: number) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/admin/applications/${id}/approve`, {
      method: 'POST',
      credentials: 'include',
    });
    if (res.ok) {
      toast.success('Application approved');
      fetchApplications(); // refresh
    } else {
      toast.error('Failed to approve');
    }
  } catch (err) {
    toast.error('Network error');
  }
};

// Reject handler
const handleReject = async (id: number) => {
  try {
    const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/admin/applications/${id}/reject`, {
      method: 'POST',
      credentials: 'include',
    });
    if (res.ok) {
      toast.success('Application rejected');
      fetchApplications(); // refresh
    } else {
      toast.error('Failed to reject');
    }
  } catch (err) {
    toast.error('Network error');
  }
};


  return (
    <div className="flex h-screen">
      {/* Sidebar */}
      <div className="w-1/4 bg-yellow-100 p-6 shadow-md">
        <h2 className="text-xl font-bold mb-4">Admin Panel</h2>
        <Button onClick={fetchApplications} className="w-full">View Applications</Button>
      </div>

      {/* Content */}
      <div className="w-3/4 p-6 overflow-y-auto">
        <h2 className="text-2xl font-semibold mb-4">Manager Applications</h2>
        {applications.length === 0 ? (
          <p>No applications yet.</p>
        ) : (
          <table className="w-full border border-gray-300 rounded-md">
            <thead>
              <tr className="bg-gray-100">
                <th className="border p-2">Student ID</th>
                <th className="border p-2">Applied Month</th>
                <th className="border p-2">Status</th>
                <th className="border p-2">Reviewed At</th>
              </tr>
            </thead>
            <tbody>
              {/* {applications.map((app: any) => (
                <tr key={app.id}>
                  <td className="border p-2">{app.stdId}</td>
                  <td className="border p-2">{app.appliedMonth}</td>
                  <td className="border p-2">{app.status}</td>
                  <td className="border p-2">{app.reviewedAt ?? 'Not reviewed'}</td>
                </tr>
              ))} */}

              {applications.map(app => (
                <div key={app.id} className="bg-white rounded-lg p-4 shadow mb-4">
                    <div className="flex justify-between items-center">
                    <div>
                        <p className="font-medium">Student ID: {app.stdId}</p>
                        <p>Month: {format(parseISO(app.appliedMonth), 'MMMM yyyy')}</p>
                    </div>
                    <div className="flex gap-2">
                        <Button variant="default" onClick={() => handleApprove(app.id)}>Approve</Button>
                        <Button variant="destructive" onClick={() => handleReject(app.id)}>Reject</Button>
                    </div>
                    </div>
                </div>
                ))}

            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;
