// // src/pages/MenuManagement.tsx
// import { useState, useEffect } from 'react';
// import { Button } from '@/components/ui/button';
// import { Input } from '@/components/ui/input';
// import { Label } from '@/components/ui/label';
// import { toast } from 'sonner';
// import { useNavigate } from 'react-router-dom';

// const MenuManagement = () => {
//   const navigate = useNavigate();
//   const [menuDate, setMenuDate] = useState('');
//   const [lunchItems, setLunchItems] = useState('');
//   const [dinnerItems, setDinnerItems] = useState('');

//   const handleSubmit = async () => {
//     try {
//       const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/menus`, {
//         method: 'POST',
//         headers: {
//           'Content-Type': 'application/json'
//         },
//         credentials: 'include',
//         body: JSON.stringify({
//           menuDate,
//           lunchItems: lunchItems.split(',').map(item => item.trim()),
//           dinnerItems: dinnerItems.split(',').map(item => item.trim()),
//         })
//       });

//       const data = await response.json();
//       if (response.ok) {
//         toast.success(data.message || 'Menu saved successfully.');
//         navigate('/manager/dashboard');
//       } else {
//         toast.error(data.message || 'Failed to save menu.');
//       }
//     } catch (error) {
//       toast.error('Network error. Please try again.');
//     }
//   };

//   return (
//     <div className="max-w-xl mx-auto py-10">
//       <h1 className="text-2xl font-bold mb-6">Create/Update Menu</h1>
//       <div className="space-y-4">
//         <div>
//           <Label>Date (YYYY-MM-DD)</Label>
//           <Input
//             type="date"
//             value={menuDate}
//             onChange={e => setMenuDate(e.target.value)}
//           />
//         </div>
//         <div>
//           <Label>Lunch Items (comma-separated)</Label>
//           <Input
//             value={lunchItems}
//             onChange={e => setLunchItems(e.target.value)}
//             placeholder="e.g. Rice, Chicken Curry, Lentils"
//           />
//         </div>
//         <div>
//           <Label>Dinner Items (comma-separated)</Label>
//           <Input
//             value={dinnerItems}
//             onChange={e => setDinnerItems(e.target.value)}
//             placeholder="e.g. Paratha, Egg Curry, Vegetables"
//           />
//         </div>
//         <Button onClick={handleSubmit} className="w-full bg-green-600 hover:bg-green-700">
//           Save Menu
//         </Button>
//       </div>
//     </div>
//   );
// };

// export default MenuManagement;


// src/pages/MenuManagement.tsx
import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { toast } from 'sonner';
import { useNavigate } from 'react-router-dom';

const MenuManagement = () => {
  const navigate = useNavigate();
  const [menuDate, setMenuDate] = useState('');
  const [lunchItems, setLunchItems] = useState('');
  const [dinnerItems, setDinnerItems] = useState('');

  const handleSubmit = async () => {
    try {
      const response = await fetch('http://localhost:8080/menus', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({
          menuDate,
          lunchItems: lunchItems.split(',').map(item => item.trim()),
          dinnerItems: dinnerItems.split(',').map(item => item.trim()),
        })
      });

      const data = await response.json();
      if (response.ok) {
        toast.success(data.message || 'Menu saved successfully.');
        navigate('/manager/dashboard');
      } else {
        toast.error(data.message || 'Failed to save menu.');
      }
    } catch (error) {
      toast.error('Network error. Please try again.');
    }
  };

  return (
    <div className="max-w-xl mx-auto py-10">
      <h1 className="text-2xl font-bold mb-6">Create/Update Menu</h1>
      <div className="space-y-4">
        <div>
          <Label htmlFor="menu-date">Date (YYYY-MM-DD)</Label>
          <Input
            id="menu-date"
            type="date"
            value={menuDate}
            onChange={e => setMenuDate(e.target.value)}
          />
        </div>
        <div>
          <Label htmlFor="lunch-items">Lunch Items (comma-separated)</Label>
          <Input
            id="lunch-items"
            value={lunchItems}
            onChange={e => setLunchItems(e.target.value)}
            placeholder="e.g. Rice, Chicken Curry, Lentils"
          />
        </div>
        <div>
          <Label htmlFor="dinner-items">Dinner Items (comma-separated)</Label>
          <Input
            id="dinner-items"
            value={dinnerItems}
            onChange={e => setDinnerItems(e.target.value)}
            placeholder="e.g. Paratha, Egg Curry, Vegetables"
          />
        </div>

        {/* <div>
          <Label>Date (YYYY-MM-DD)</Label>
          <Input
            type="date"
            value={menuDate}
            onChange={e => setMenuDate(e.target.value)}
          />
        </div>
        <div>
          <Label>Lunch Items (comma-separated)</Label>
          <Input
            value={lunchItems}
            onChange={e => setLunchItems(e.target.value)}
            placeholder="e.g. Rice, Chicken Curry, Lentils"
          />
        </div>
        <div>
          <Label>Dinner Items (comma-separated)</Label>
          <Input
            value={dinnerItems}
            onChange={e => setDinnerItems(e.target.value)}
            placeholder="e.g. Paratha, Egg Curry, Vegetables"
          />
        </div> */}
        <Button onClick={handleSubmit} className="w-full bg-green-600 hover:bg-green-700">
          Save Menu
        </Button>
      </div>
    </div>
  );
};

export default MenuManagement;

