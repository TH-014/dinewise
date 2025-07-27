import { useEffect, useState } from 'react';
import { Button } from '@/components/ui/button';
import { Checkbox } from '@/components/ui/checkbox';
import { format } from 'date-fns';
import { toast } from 'sonner';

const ManagerStats = () => {
  const [date, setDate] = useState('');
  const [items, setItems] = useState<string[]>([]);
  const [selectedItems, setSelectedItems] = useState<string[]>([]);
  const [results, setResults] = useState<{ itemName: string, lunchCount: number, dinnerCount: number }[]>([]);

  const fetchItems = async () => {
    if (!date) return;
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/menus/distinct-items?fromDate=${date}`, { credentials: 'include' });
      if (!response.ok) throw new Error("Couldn't load items");
      const data = await response.json();
      setItems(data);
    } catch (err) {
      toast.error((err as Error).message);
    }
  };

  const fetchStats = async () => {
    try {
      const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/menus/count-multiple?fromDate=${date}`, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(selectedItems),
      });
      if (!response.ok) throw new Error("Couldn't load stats");
      const data = await response.json();
      setResults(data);
    } catch (err) {
      toast.error((err as Error).message);
    }
  };

  return (
    <div className="p-6 max-w-4xl mx-auto space-y-6">
      <h1 className="text-2xl font-bold">Menu Stats From Date</h1>
      <input
        type="date"
        value={date}
        onChange={e => setDate(e.target.value)}
        className="border p-2 rounded-md"
      />
      <Button onClick={fetchItems} className="bg-green-600 text-white">
        Load Items
      </Button>

      <div className="grid grid-cols-2 gap-2">
        {items.map(item => (
          <label key={item} className="flex items-center gap-2">
            <Checkbox
              checked={selectedItems.includes(item)}
              onCheckedChange={checked =>
                setSelectedItems(prev =>
                  checked ? [...prev, item] : prev.filter(i => i !== item)
                )
              }
            />
            {item}
          </label>
        ))}
      </div>

      <Button onClick={fetchStats} className="bg-blue-600 text-white">
        Show Stats
      </Button>

      {results.length > 0 && (
        <table className="table-auto w-full mt-6 border">
          <thead>
            <tr>
              <th className="border px-4 py-2">Item</th>
              <th className="border px-4 py-2">Lunch Count</th>
              <th className="border px-4 py-2">Dinner Count</th>
            </tr>
          </thead>
          <tbody>
            {results.map(row => (
              <tr key={row.itemName}>
                <td className="border px-4 py-2">{row.itemName}</td>
                <td className="border px-4 py-2">{row.lunchCount}</td>
                <td className="border px-4 py-2">{row.dinnerCount}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ManagerStats;
