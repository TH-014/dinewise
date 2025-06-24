// import { useEffect, useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import { toast } from 'sonner';
// // Removed invalid top-level useState hook

// type StockItem = {
//   id: number;
//   itemName: string;
//   unit: string;
//   quantity: number;
//   perUnitPrice: number;
// };



// const ManagerStockView = () => {
//   const [stocks, setStocks] = useState([]);
//   const navigate = useNavigate();

//   useEffect(() => {
//     const fetchStocks = async () => {
//       try {
//         const response = await fetch('http://localhost:8080/stocks', {
//           credentials: 'include',
//         });
//         if (!response.ok) throw new Error('Failed to fetch stock data');
//         const data = await response.json();
//         setStocks(data);
//       } catch (err) {
//         console.error(err);
//         toast.error('Unable to load stock data.');
//       }
//     };

//     fetchStocks();
//   }, []);

//   return (
//     <div className="max-w-5xl mx-auto py-8">
//       <h1 className="text-3xl font-bold mb-6 text-center">Current Stock</h1>
//       <div className="overflow-x-auto">
//         <table className="min-w-full table-auto border border-gray-300 shadow">
//           <thead className="bg-gray-100">
//             <tr>
//               <th className="p-3 border">Item</th>
//               <th className="p-3 border">Unit</th>
//               <th className="p-3 border">Quantity</th>
//               <th className="p-3 border">Per Unit Price</th>
//               <th className="p-3 border">Last Updated</th>
//             </tr>
//           </thead>
//           <tbody>
//             {/* {stocks.map((stock: any) => (
//               <tr key={stock.id} className="text-center border-t">
//                 <td className="p-2 border">{stock.itemName}</td>
//                 <td className="p-2 border">{stock.unit}</td>
//                 <td className="p-2 border">{stock.quantity}</td>
//                 <td className="p-2 border">{stock.perUnitPrice}</td>
//                 <td className="p-2 border">{new Date(stock.lastUpdated).toLocaleString()}</td>
//               </tr>
//             ))} */}
//             {stocks.map((stock) => (
//             <tr key={stock.id} className="text-center border-t">
//                 <td className="p-2 border">{stock.itemName}</td>
//                 <td className="p-2 border">{stock.unit}</td>
//                 <td className="p-2 border">{stock.quantity}</td>
//                 <td className="p-2 border">{stock.perUnitPrice}</td>
//             </tr>
//             ))}

//           </tbody>
//         </table>
//       </div>
//     </div>
//   );
// };

// export default ManagerStockView;

// src/pages/StockPage.tsx
import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { toast } from 'sonner';

interface StockItem {
  id: number;
  itemName: string;
  unit: string;
  quantity: number;
  perUnitPrice: number;
  lastUpdated: string;
}

const ManagerStockView = () => {
  const [stocks, setStocks] = useState<StockItem[]>([]);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editItem, setEditItem] = useState<StockItem | null>(null);
  const [formData, setFormData] = useState({
    itemName: '',
    unit: '',
    quantity: '',
    perUnitPrice: ''
  });

  const fetchStocks = async () => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/stocks`, {
        credentials: 'include',
      });
      const data = await response.json();
      setStocks(data);
    } catch (err) {
      toast.error('Failed to load stock data');
    }
  };

  useEffect(() => {
    fetchStocks();
  }, []);

  const handleInputChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    const endpoint = editItem ? `${import.meta.env.VITE_API_BASE_URL}/stocks/${editItem.id}` : `${import.meta.env.VITE_API_BASE_URL}/stocks`;
    const method = editItem ? 'PUT' : 'POST';

    try {
      const response = await fetch(endpoint, {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        toast.success(editItem ? 'Stock updated' : 'New stock item added');
        setDialogOpen(false);
        setFormData({ itemName: '', unit: '', quantity: '', perUnitPrice: '' });
        setEditItem(null);
        fetchStocks();
      } else {
        toast.error('Error saving data');
      }
    } catch {
      toast.error('Network error');
    }
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Stock Inventory</h1>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button onClick={() => setEditItem(null)}>Add Item</Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>{editItem ? 'Update Stock Item' : 'Add New Item'}</DialogTitle>
            </DialogHeader>
            <div className="space-y-4">
              <Input name="itemName" placeholder="Item Name" value={formData.itemName} onChange={handleInputChange} />
              <Input name="unit" placeholder="Unit (e.g. kg, litre)" value={formData.unit} onChange={handleInputChange} />
              <Input name="quantity" type="number" placeholder="Quantity" value={formData.quantity} onChange={handleInputChange} />
              <Input name="perUnitPrice" type="number" placeholder="Price per Unit" value={formData.perUnitPrice} onChange={handleInputChange} />
              <Button onClick={handleSubmit}>{editItem ? 'Update' : 'Add'}</Button>
            </div>
          </DialogContent>
        </Dialog>
      </div>

      <table className="w-full border mt-4 text-sm">
        <thead>
          <tr className="bg-gray-100">
            <th className="p-2 border">Item Name</th>
            <th className="p-2 border">Unit</th>
            <th className="p-2 border">Quantity</th>
            <th className="p-2 border">Price/Unit</th>
            <th className="p-2 border">Last Updated</th>
            <th className="p-2 border">Action</th>
          </tr>
        </thead>
        <tbody>
          {stocks.map((stock) => (
            <tr key={stock.id} className="text-center border-t">
              <td className="p-2 border">{stock.itemName}</td>
              <td className="p-2 border">{stock.unit}</td>
              <td className="p-2 border">{stock.quantity}</td>
              <td className="p-2 border">{stock.perUnitPrice}</td>
              <td className="p-2 border">{new Date(stock.lastUpdated).toLocaleString()}</td>
              <td className="p-2 border">
                <Button
                  size="sm"
                  onClick={() => {
                    setEditItem(stock);
                    setFormData({
                      itemName: stock.itemName,
                      unit: stock.unit,
                      quantity: String(stock.quantity),
                      perUnitPrice: String(stock.perUnitPrice),
                    });
                    setDialogOpen(true);
                  }}
                >
                  Edit
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default  ManagerStockView;

