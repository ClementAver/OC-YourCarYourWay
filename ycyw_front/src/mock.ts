import { Chat, Message, User } from './interfaces';

const users: User[] = [
  {
    id: 0,
    email: 'john.dalton@mail.me',
    name: 'John Dalton',
    role: 'employee',
  },
  {
    id: 1,
    email: 'jack.dalton@mail.me',
    name: 'Jack Dalton',

    role: 'customer',
  },
];

const messages: Array<Message> = [
  {
    id: 0,
    content:
      "Bonjour, j'aimerais une information concernant les pleins d'essence.",
    publication_time: new Date('2023-10-01T12:00:00Z'),
    author_id: 1,
    chat_id: 0,
  },
  {
    id: 1,
    content:
      "Bonjour, le véhicule vous est fourni avec un plein d'essence. Vous devez le rendre avec le même niveau d'essence.",
    publication_time: new Date('2023-10-01T12:01:00Z'),
    author_id: 0,
    chat_id: 0,
  },
  {
    id: 2,
    content: 'Merci pour votre réponse rapide !',
    publication_time: new Date('2023-10-01T12:02:00Z'),
    author_id: 1,
    chat_id: 0,
  },
  {
    id: 3,
    content: "Bonjour, êtes-vous présent dans d'autres pays ?",
    publication_time: new Date('2023-11-01T12:00:00Z'),
    author_id: 1,
    chat_id: 1,
  },
  {
    id: 4,
    content: 'Bonjour, oui dans le monde entier.',
    publication_time: new Date('2023-11-01T12:01:00Z'),
    author_id: 0,
    chat_id: 1,
  },
  {
    id: 5,
    content: 'Très bien, je vous remercie !',
    publication_time: new Date('2023-11-01T12:02:00Z'),
    author_id: 1,
    chat_id: 1,
  },
];

const chats: Chat[] = [
  { id: 0, employee_id: 0, customer_id: 1 },
  { id: 1, employee_id: 0, customer_id: 1 },
];

export { users, messages, chats };
