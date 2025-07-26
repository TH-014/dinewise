
// src/pages/StockPage.tsx


// import { useEffect, useState } from 'react';
// import { Button } from '@/components/ui/button';
// import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '@/components/ui/dialog';
// import { Input } from '@/components/ui/input';
// import { toast } from 'sonner';

// interface StockItem {
//   id: number;
//   itemName: string;
//   unit: string;
//   quantity: number;
//   perUnitPrice: number;
//   lastUpdated: string;
// }

// const ManagerStockView = () => {
//   const [stocks, setStocks] = useState<StockItem[]>([]);
//   const [dialogOpen, setDialogOpen] = useState(false);
//   const [editItem, setEditItem] = useState<StockItem | null>(null);
//   const [formData, setFormData] = useState({
//     itemName: '',
//     unit: '',
//     quantity: '',
//     perUnitPrice: ''
//   });

//   const fetchStocks = async () => {
//     try {
//       const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/stocks`, {
//         credentials: 'include',
//       });
//       const data = await response.json();
//       setStocks(data);
//     } catch (err) {
//       toast.error('Failed to load stock data');
//     }
//   };

//   useEffect(() => {
//     fetchStocks();
//   }, []);

//   const handleInputChange = (e) => {
//     setFormData({ ...formData, [e.target.name]: e.target.value });
//   };

//   const handleSubmit = async () => {
//     const endpoint = editItem ? `${import.meta.env.VITE_API_BASE_URL}/stocks/${editItem.id}` : `${import.meta.env.VITE_API_BASE_URL}/stocks`;
//     const method = editItem ? 'PUT' : 'POST';

//     try {
//       const response = await fetch(endpoint, {
//         method,
//         headers: { 'Content-Type': 'application/json' },
//         credentials: 'include',
//         body: JSON.stringify(formData),
//       });

//       if (response.ok) {
//         toast.success(editItem ? 'Stock updated' : 'New stock item added');
//         setDialogOpen(false);
//         setFormData({ itemName: '', unit: '', quantity: '', perUnitPrice: '' });
//         setEditItem(null);
//         fetchStocks();
//       } else {
//         toast.error('Error saving data');
//       }
//     } catch {
//       toast.error('Network error');
//     }
//   };

//   return (
//     <div className="p-6 max-w-4xl mx-auto">
//       <div className="flex justify-between items-center mb-4">
//         <h1 className="text-2xl font-bold">Stock Inventory</h1>
//         <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
//           <DialogTrigger asChild>
//             <Button onClick={() => setEditItem(null)}>Add Item</Button>
//           </DialogTrigger>
//           <DialogContent>
//             <DialogHeader>
//               <DialogTitle>{editItem ? 'Update Stock Item' : 'Add New Item'}</DialogTitle>
//             </DialogHeader>
//             <div className="space-y-4">
//               <Input name="itemName" placeholder="Item Name" value={formData.itemName} onChange={handleInputChange} />
//               <Input name="unit" placeholder="Unit (e.g. kg, litre)" value={formData.unit} onChange={handleInputChange} />
//               <Input name="quantity" type="number" placeholder="Quantity" value={formData.quantity} onChange={handleInputChange} />
//               <Input name="perUnitPrice" type="number" placeholder="Price per Unit" value={formData.perUnitPrice} onChange={handleInputChange} />
//               <Button onClick={handleSubmit}>{editItem ? 'Update' : 'Add'}</Button>
//             </div>
//           </DialogContent>
//         </Dialog>
//       </div>

//       <table className="w-full border mt-4 text-sm">
//         <thead>
//           <tr className="bg-gray-100">
//             <th className="p-2 border">Item Name</th>
//             <th className="p-2 border">Unit</th>
//             <th className="p-2 border">Quantity</th>
//             <th className="p-2 border">Price/Unit</th>
//             <th className="p-2 border">Last Updated</th>
//             <th className="p-2 border">Action</th>
//           </tr>
//         </thead>
//         <tbody>
//           {stocks.map((stock) => (
//             <tr key={stock.id} className="text-center border-t">
//               <td className="p-2 border">{stock.itemName}</td>
//               <td className="p-2 border">{stock.unit}</td>
//               <td className="p-2 border">{stock.quantity}</td>
//               <td className="p-2 border">{stock.perUnitPrice}</td>
//               <td className="p-2 border">{new Date(stock.lastUpdated).toLocaleString()}</td>
//               <td className="p-2 border">
//                 <Button
//                   size="sm"
//                   onClick={() => {
//                     setEditItem(stock);
//                     setFormData({
//                       itemName: stock.itemName,
//                       unit: stock.unit,
//                       quantity: String(stock.quantity),
//                       perUnitPrice: String(stock.perUnitPrice),
//                     });
//                     setDialogOpen(true);
//                   }}
//                 >
//                   Edit
//                 </Button>
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   );
// };

// export default  ManagerStockView;



import { useEffect, useState } from 'react';
import {
  Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogFooter
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { toast } from 'sonner';
import { Label } from '@/components/ui/label';
import {
  Select, SelectContent, SelectItem, SelectTrigger, SelectValue
} from '@/components/ui/select';

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

  // Add/Subtract Dialog States
  const [addDialog, setAddDialog] = useState(false);
  const [subDialog, setSubDialog] = useState(false);
  const [targetStock, setTargetStock] = useState<StockItem | null>(null);
  const [addQty, setAddQty] = useState('');
  const [newPrice, setNewPrice] = useState('');
  const [subQty, setSubQty] = useState('');
  const [usedFor, setUsedFor] = useState<'lunch' | 'dinner'>('lunch');

  const fetchStocks = async () => {
    try {
      const response = await fetch('${import.meta.env.VITE_API_BASE_URL}/stocks', {
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
    const endpoint = editItem
      ? `${import.meta.env.VITE_API_BASE_URL}/stocks/${editItem.id}`
      : `${import.meta.env.VITE_API_BASE_URL}/stocks`;
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

  const handleAddStock = async () => {
    const qty = parseFloat(addQty);
    const price = newPrice ? parseFloat(newPrice) : -1 ;
    if (!qty || qty <= 0 || !targetStock) return toast.error('Invalid quantity');

    const url = new URL(`${import.meta.env.VITE_API_BASE_URL}/stocks/${targetStock.id}/add`);
    url.searchParams.append('quantity', qty.toString());
    url.searchParams.append('price', price.toString());

    const res = await fetch(url.toString(), {
      method: 'POST',
      credentials: 'include',
    });

    if (res.ok) {
      toast.success('Stock added');
      setAddDialog(false);
      setAddQty('');
      setNewPrice('');
      fetchStocks();
    } else toast.error('Failed to add stock');
  };

  const handleSubtractStock = async () => {
    const qty = parseFloat(subQty);
    if (!qty || qty <= 0 || !targetStock) return toast.error('Invalid quantity');

    const url = new URL(`${import.meta.env.VITE_API_BASE_URL}/stocks/${targetStock.id}/subtract`);
    url.searchParams.append('quantity', qty.toString());
    url.searchParams.append('usedFor', usedFor);

    const res = await fetch(url.toString(), {
      method: 'POST',
      credentials: 'include',
    });

    if (res.ok) {
      toast.success('Stock used');
      setSubDialog(false);
      setSubQty('');
      fetchStocks();
    } else toast.error('Failed to subtract stock');
  };

  return (
    <div className="p-6 max-w-5xl mx-auto">
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
              <td className="p-2 border space-x-2">
                {/* <Button
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
                </Button> */}
                <Button size="sm" variant="outline" onClick={() => {
                  setTargetStock(stock);
                  setAddDialog(true);
                }}>
                  Add
                </Button>
                <Button size="sm" variant="destructive" onClick={() => {
                  setTargetStock(stock);
                  setSubDialog(true);
                }}>
                  Subtract
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Add Dialog */}
      <Dialog open={addDialog} onOpenChange={setAddDialog}>
        <DialogContent>
          <DialogHeader><DialogTitle>Add to Stock</DialogTitle></DialogHeader>
          <div className="space-y-4">
            <div>
              <Label>Quantity</Label>
              <Input
                type="number"
                value={addQty}
                onChange={(e) => setAddQty(e.target.value)}
                placeholder="Enter quantity"
              />
            </div>
            <div>
              <Label>New Price (optional)</Label>
              <Input
                type="number"
                value={newPrice}
                onChange={(e) => setNewPrice(e.target.value)}
                placeholder={`Current: ${targetStock?.perUnitPrice}`}
              />
            </div>
          </div>
          <DialogFooter>
            <Button onClick={handleAddStock}>Confirm Add</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Subtract Dialog */}
      <Dialog open={subDialog} onOpenChange={setSubDialog}>
        <DialogContent>
          <DialogHeader><DialogTitle>Use from Stock</DialogTitle></DialogHeader>
          <div className="space-y-4">
            <div>
              <Label>Quantity</Label>
              <Input
                type="number"
                value={subQty}
                onChange={(e) => setSubQty(e.target.value)}
                placeholder="Enter quantity"
              />
            </div>
            <div>
              <Label>Used For</Label>
              <Select value={usedFor} onValueChange={(v) => setUsedFor(v as 'lunch' | 'dinner')}>
                <SelectTrigger><SelectValue placeholder="Select" /></SelectTrigger>
                <SelectContent>
                  <SelectItem value="lunch">Lunch</SelectItem>
                  <SelectItem value="dinner">Dinner</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
          <DialogFooter>
            <Button onClick={handleSubtractStock}>Confirm Subtract</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default ManagerStockView;


