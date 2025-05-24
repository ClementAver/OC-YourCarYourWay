import { Message, User } from '../interfaces';
import { chats, users, messages } from '../mock';

export function getUser(userId: number) {
  return users.find((user) => user.id === userId) || null;
}

export function getChat(chatId: number) {
  return chats.find((chat) => chat.id === chatId) || null;
}

export function getMessages(chatId: number) {
  return messages.filter((message) => message.chat_id === chatId);
}

export function postMessage(
  chatId: number,
  authorId: number,
  content: string,
  messages: Message[]
): void {
  const newMessage = {
    id: messages.length,
    content,
    publication_time: new Date(),
    author_id: authorId,
    chat_id: chatId,
  };
  messages.push(newMessage);
}
