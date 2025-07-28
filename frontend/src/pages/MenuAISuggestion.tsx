import { useState } from "react";
import { Button } from "@/components/ui/button";
import { format } from "date-fns";

export default function MenuAISuggestion() {
  const [date, setDate] = useState(new Date());
  const [mode, setMode] = useState("normal");
  const [allowShopping, setAllowShopping] = useState(false);
  const [result, setResult] = useState("");

  const fetchSuggestion = async () => {
    const formattedDate = format(date, "yyyy-MM-dd");
    const res = await fetch(
      `${import.meta.env.VITE_API_BASE_URL}/ai/menu-suggestion?date=${formattedDate}&mode=${mode}&allowShopping=${allowShopping}`,
      {
        credentials: "include",
      }
    );
    const data = await res.json();
    setResult(data.suggestion);
  };

  return (
    <div className="p-4 space-y-4">
      <h2 className="text-xl font-semibold">AI Menu Suggestion</h2>

      <label>Date: <input type="date" value={format(date, "yyyy-MM-dd")} onChange={e => setDate(new Date(e.target.value))} /></label>

      <div className="space-y-2">
        <label>Menu Type:</label>
        <select value={mode} onChange={e => setMode(e.target.value)}>
          <option value="normal-both">Normal</option>
          <option value="improvement-lunch">Improve Lunch</option>
          <option value="improvement-dinner">Improve Dinner</option>
          <option value="improvement-both">Improve Both</option>
          <option value="fest-lunch">Fest Lunch</option>
          <option value="fest-dinner">Fest Dinner</option>
        </select>
      </div>

      <label className="flex gap-2 items-center">
        <input type="checkbox" checked={allowShopping} onChange={e => setAllowShopping(e.target.checked)} />
        Allow Shopping if required
      </label>

      <Button onClick={fetchSuggestion}>Generate Suggestion</Button>

      {result && (
        <div className="mt-4 whitespace-pre-wrap bg-gray-100 p-4 rounded-md">
          {result}
        </div>
      )}
    </div>
  );
}
