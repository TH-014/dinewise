// src/pages/CommentsThreadPage.tsx
import { useLocation } from 'react-router-dom';
import CommentsThread from '@/components/CommentsThread';
import { parseISO } from 'date-fns';
import { useMemo } from 'react';

export default function CommentsThreadPage() {
  const location = useLocation();
  const params = new URLSearchParams(location.search);
  const dateStr = params.get('date');

  const menuDate = useMemo(() => {
    return dateStr ? parseISO(dateStr) : new Date();
  }, [dateStr]);

  return (
    <div className="max-w-3xl mx-auto py-10 px-4">
      <CommentsThread menuDate={menuDate} />
    </div>
  );
}
