// src/components/CommentsThread.tsx
import { useEffect, useState } from 'react';
import { format, parseISO } from 'date-fns';
import { Button } from '@/components/ui/button';
import { Popover } from '@/components/ui/popover';
import { Textarea } from '@/components/ui/textarea';
import { toast } from 'sonner';



interface Comment {
  id: number;
  parentId: number | null;
  anonymous: boolean;
  authorId: string;
  authorRole: string;
  content: string;
  createdAt: string;
}

export default function CommentsThread({ menuDate }: { menuDate: Date }) {
  const [comments, setComments] = useState<Comment[]>([]);
  const [replyTo, setReplyTo] = useState<Comment | null>(null);
  const [content, setContent] = useState("");
  const [anon, setAnon] = useState(false);
  // const params = new URLSearchParams(location.search);
  // const dateStr = params.get('date')!;
  // const date = parseISO(dateStr);

//   return <CommentsThread menuDate={date} />

  const fetchComments = async () => {
    const res = await fetch(`http://localhost:8080/comments?date=${format(menuDate, 'yyyy-MM-dd')}`, { credentials: 'include' });
    const data: Comment[] = await res.json();
    setComments(data);
  };

  useEffect(() => { fetchComments() }, [menuDate]);

  // const post = async () => {
  //   const payload = {
  //     menuDate: format(menuDate,'yyyy-MM-dd'),
  //     parentId: replyTo?.id,
  //     anonymous: anon,
  //     content
  //   };
  //   const res = await fetch('/comments', {
  //     method: 'POST',
  //     headers: {'Content-Type':'application/json'},
  //     credentials: 'include',
  //     body: JSON.stringify(payload)
  //   });
  //   if (res.ok) {
  //     toast.success("Posted!");
  //     setContent("");
  //     setAnon(false);
  //     setReplyTo(null);
  //     fetchComments();
  //   } else toast.error("Failed to post.");
  // };


  const post = async () => {
  const payload = {
    menuDate: format(menuDate,'yyyy-MM-dd'),
    parentId: replyTo?.id,
    anonymous: anon,
    content
  };

  try {
    const res = await fetch('http://localhost:8080/comments', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(payload)
    });

    console.log(res);

    if (!res.ok) {
      const err = await res.text();
      console.error("Error posting:", err);
      toast.error(`Failed to post: ${res.status}`);
    } else {
      toast.success("Posted!");
      setContent("");
      setAnon(false);
      setReplyTo(null);
      fetchComments();
    }
  } catch (err) {
    console.error("Network error:", err);
    toast.error("Network error");
  }
};


  const renderTree = (parentId: number | null = null, level = 0) => {
    return comments
      .filter(c => c.parentId === parentId)
      .map(c => (
        <div key={c.id} className={`ml-${level*4} p-2 border rounded`}>
          <p><small>
            {c.anonymous ? "Anonymous" : c.authorId} • {format(new Date(c.createdAt), 'PP p')}
          </small></p>
          <p>{c.content}</p>
          <Button size="sm" variant="link" onClick={() => setReplyTo(c)}>Reply</Button>
          {renderTree(c.id, level + 1)}
        </div>
      ));
  };

  return (
    <div>
      <h3 className="text-xl font-bold mb-2">Comments</h3>
      {renderTree()}
      <div className="mt-4 p-4 border rounded bg-gray-50 space-y-2">
        <p>{replyTo ? `Replying to @${replyTo.authorId}:` : "New comment"}</p>
        <Textarea
          className="w-full"
          rows={3} value={content}
          onChange={e => setContent(e.target.value)}
        />
        <div>
          <label className="inline-flex items-center space-x-2">
            <input
              type="checkbox" checked={anon}
              onChange={e => setAnon(e.target.checked)}
            />
            <span>Anonymously</span>
          </label>
        </div>
        <Button onClick={post} disabled={!content}>Submit</Button>
        {replyTo && <Button variant="ghost" onClick={() => setReplyTo(null)}>Cancel</Button>}
      </div>
    </div>
  );
}
