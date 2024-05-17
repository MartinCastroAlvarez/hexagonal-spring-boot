// Api.tsx

import axios from 'axios';

import { Meeting, Message, Product, Purchase, Sale, Schedule, User, Work, Pto, File } from './Models';
import { store, logout, TOKEN_STORAGE_KEY } from './Store';
import { notifyError, notifyMessage  } from './Utils';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

api.interceptors.request.use(function (config: any) {
  console.log(`API Request: ${config.method.toUpperCase()} ${config.baseURL} ${config.url} ${config.params ? JSON.stringify(config.params) : JSON.stringify(config.data)}`);
  // notifyMessage(`${config.method?.toUpperCase()} ${config.url}`);
  const token = localStorage.getItem(TOKEN_STORAGE_KEY);
  if (token && token !== undefined && token !== null && token !== 'undefined') {
    config.headers.Authorization = `Bearer ${token}`;
    console.log(`API Request with Token: ${token}`);
  }
  return config;
}, function (error: any) {
  return Promise.reject(error);
});

api.interceptors.response.use(function (response: any) {
  console.log(`API Response: ${JSON.stringify(response)}`);
  // notifyMessage(`Response: ${response.status} ${response.config.url}`);
  return Promise.resolve(response);
}, function (error: any) {
  console.error(`API Error: ${JSON.stringify(error)}`);
  if (error.response) {
    notifyError(`Error ${error.response.status}: ${error.response?.data?.Error || error.response?.statusText || error.config.url}`);
    if (error.response.status === 401) {
      console.log('Unauthorized access, logging out...');
      notifyError(`Your session has expired!`);
      store.dispatch(logout());
    }
  } else {
    notifyError(`Fatal Error: ${error.message}`);
  }
  return Promise.resolve(error);
});

export const Api = {
  createMeeting: async (title: string, day: Date) => {
    console.log(`Creating meeting ${title} on ${day}`);
    if (!day) {
      notifyError('Invalid date!');
      return;
    }
    if (!title) {
      notifyError('Invalid title!');
      return;
    }
    const response: any = await api.post<Meeting>('/meetings', {title, day});
    if (response.data)
      notifyMessage(`Meeting ${title} created!`);
    return response.data;
  },
  createProduct: async (name: string, price: number) => {
    console.log(`Creating product ${name}`);
    if (!name) {
      notifyError('Invalid name!');
      return;
    }
    if (!price) {
      notifyError('Invalid price!');
      return;
    }
    const response: any = await api.post<Product>('/products', {name, price});
    if (response.data)
      notifyMessage(`Product ${name} created!`);
    return response.data;
  },
  checkIn: async () => {
    console.log(`Checking in`);
    const response: any = await api.post<Work>(`/work/checkin`);
    if (response.data)
      notifyMessage(`Checked in!`);
    return response.data;
  },
  checkOut: async () => {
    console.log(`Checking out`);
    const response: any = await api.post<Work>(`/work/checkout`);
    if (response.data)
      notifyMessage(`Checked out!`);
    return response.data;
  },
  createMessage: async (text: string, subject: string) => {
    console.log(`Creating message ${subject}`);
    if (!text) {
      notifyError('Invalid text!');
      return;
    }
    if (!subject) {
      notifyError('Invalid subject!');
      return;
    }
    const response: any = await api.post<Message>('/messages', {text, subject});
    if (response.data)
      notifyMessage(`Message ${subject} created!`);
    return response.data;
  },
  addUserToMessage: async (messageId: number, userId: number) => {
    console.log(`Adding user ${userId} to message ${messageId}`);
    if (!userId) {
      notifyError('Invalid user!');
      return;
    }
    if (!messageId) {
      notifyError('Invalid message!');
      return;
    }
    const response: any = await api.post<void>(`/messages/${messageId}/users/${userId}`);
    if (response.data)
      notifyMessage(`User added to message!`);
    return response.data;
  },
  addUserToMeeting: async (meetingId: number, userId: number) => {
    console.log(`Adding user ${userId} to meeting ${meetingId}`);
    if (!userId) {
      notifyError('Invalid user!');
      return;
    }
    const response: any = await api.post<void>(`/meetings/${meetingId}/users/${userId}`);
    if (response.data)
      notifyMessage(`User added to meeting!`);
    return response.data;
  },
  createSchedule: async (userId: number, start: string, end: string, dayOfWeek: string) => {
    console.log(`Adding schedule to user ${userId}`);
    if (!start) {
      notifyError('Invalid start!');
      return;
    }
    if (!end) {
      notifyError('Invalid end!');
      return;
    }
    if (!dayOfWeek) {
      notifyError('Invalid day of week!');
      return;
    }
    if (!userId) {
      notifyError('Invalid user!');
      return;
    }
    const response: any = await api.post<Schedule>(`/users/${userId}/schedule`, {start, end, dayOfWeek: dayOfWeek.toUpperCase()});
    if (response.data)
      notifyMessage(`Schedule added to user!`);
    return response.data;
  },
  createPto: async (userId: number, day: Date, type: string) => {
    console.log(`Adding pto to user ${userId}`);
    if (!day) {
      notifyError('Invalid day!');
      return;
    }
    if (!type) {
      notifyError('Invalid type!');
      return;
    }
    if (!userId) {
      notifyError('Invalid user!');
      return;
    }
    const response: any = await api.post<Pto>(`/users/${userId}/pto`, {day, type: type.toUpperCase()});
    if (response.data)
      notifyMessage(`Pto added to user!`);
    return response.data;
  },
  createSale: async (productId: number, amount: number, price: number) => {
    console.log(`Selling product ${productId}`);
    if (!amount) {
      notifyError('Invalid amount!');
      return;
    }
    if (!price) {
      notifyError('Invalid price!');
      return;
    }
    if (!productId) {
      notifyError('Invalid product!');
      return;
    }
    const response: any = await api.post(`/sales/${productId}`, {amount, price});
    if (response.data)
      notifyMessage(`Sale added to product!`);
    return response.data;
  },
  createPurchase: async (productId: number, amount: number, cost: number) => {
    console.log(`Adding stock to product ${productId}`);
    if (!amount) {
      notifyError('Invalid amount!');
      return;
    }
    if (!cost) {
      notifyError('Invalid cost!');
      return;
    }
    if (!productId) {
      notifyError('Invalid product!');
      return;
    }
    const response: any = await api.post(`/purchases/${productId}`, {amount, cost});
    if (response.data)
      notifyMessage(`Purchase added to product!`);
    return response.data;
  },
  updateProduct: async (productId: number, name: string, price: number) => {
    console.log(`Updating product ${productId}`);
    if (!name) {
      notifyError('Invalid name!');
      return;
    }
    if (!price) {
      notifyError('Invalid price!');
      return;
    }
    if (!productId) {
      notifyError('Invalid product!');
      return;
    }
    const response: any = await api.put<Product>(`/products/${productId}`, {name, price});
    if (response.data)
      notifyMessage(`Product updated!`);
    return response.data;
  },
  updateUser: async (userId: number, name: string) => {
    console.log(`Updating user ${userId}`);
    if (!name) {
      notifyError('Invalid name!');
      return;
    }
    if (!userId) {
      notifyError('Invalid user!');
      return;
    }
    const response: any = await api.put<User>(`/users/${userId}`, {name});
    if (response.data)
      notifyMessage(`User updated!`);
    return response.data;
  },
  sendMessage: async (messageId: number) => {
    console.log(`Sending message ${messageId}`);
    if (!messageId) {
      notifyError('Invalid message!');
      return;
    }
    const response: any = await api.post<Message>(`/messages/${messageId}`);
    if (response.data)
      notifyMessage(`Message sent!`);
    return response.data;
  },
  uploadFileToMeeting: async (meetingId: number, file: Blob, name: string) => {
    const formData = new FormData();
    formData.append('file', file as Blob, name);
    formData.append('fileName', name);
    const response = await api.post<File>(`/meetings/${meetingId}/files`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    if (response.data)
      notifyMessage('File uploaded successfully!');
    return response.data;
  },
  downloadFile: async (meetingId: number, fileId: number) => {
    console.log(`Downloading file ${fileId} from meeting ${meetingId}`);
    if (!fileId) {
      notifyError('Invalid file!');
      return;
    }
    if (!meetingId) {
      notifyError('Invalid meeting!');
      return;
    }
    const response = await api.get(`/meetings/${meetingId}/files/${fileId}`, { responseType: 'blob' });
    if (!response) {
      notifyError('File not found!');
      return;
    }
    console.log('Response Headers:', response.headers);
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    const contentDisposition = response.headers['hexfilemetadata'];
    let fileName = 'downloaded-file';
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename="?([^"]+)"?/);
      if (fileNameMatch && fileNameMatch.length > 1) {
        fileName = fileNameMatch[1];
      }
    }
    link.setAttribute('download', fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  },
  getProduct: async (productId: number) => {
    console.log(`Getting product ${productId}`);
    const response: any = await api.get<Product>(`/products/${productId}`);
    return response.data;
  },
  getUser: async (userId: number) => {
    console.log(`Getting user ${userId}`);
    const response: any = await api.get<User>(`/users/${userId}`);
    return response.data;
  },
  getSale: async (saleId: number) => {
    console.log(`Getting sale ${saleId}`);
    const response: any = await api.get<Sale>(`/sales/${saleId}`);
    return response.data;
  },
  getPurchase: async (purchaseId: number) => {
    console.log(`Getting purchase ${purchaseId}`);
    const response: any = await api.get<Purchase>(`/purchases/${purchaseId}`);
    return response.data;
  },
  getWork: async (workId: number) => {
    console.log(`Getting work ${workId}`);
    const response: any = await api.get<Work>(`/work/${workId}`);
    return response.data;
  },
  getSchedule: async (scheduleId: number) => {
    console.log(`Getting schedule ${scheduleId}`);
    const response: any = await api.get<Schedule>(`/schedule/${scheduleId}`);
    return response.data;
  },
  getPto: async (ptoId: number) => {
    console.log(`Getting pto ${ptoId}`);
    const response: any = await api.get<Pto>(`/pto/${ptoId}`);
    return response.data;
  },
  getMeeting: async (meetingId: number) => {
    console.log(`Getting meeting ${meetingId}`);
    const response: any = await api.get<Meeting>(`/meetings/${meetingId}`);
    return response.data;
  },
  getMessage: async (messageId: number) => {
    console.log(`Getting message ${messageId}`);
    const response: any = await api.get<Message>(`/messages/${messageId}`);
    return response.data;
  },
  deleteMeeting: async (meetingId: number) => {
    console.log(`Deleting meeting ${meetingId}`);
    const response: any = await api.delete(`/meetings/${meetingId}`);
    if (response.data)
      notifyMessage(`Meeting deleted!`);
    return response.data;
  },
  deleteMessage: async (messageId: number) => {
    console.log(`Deleting message ${messageId}`);
    const response: any = await api.delete(`/messages/${messageId}`);
    if (response.data)
      notifyMessage(`Message deleted!`);
    return response.data;
  },
  deleteProduct: async (productId: number) => {
    console.log(`Deleting product ${productId}`);
    const response: any = await api.delete(`/products/${productId}`);
    if (response.data)
      notifyMessage(`Product deleted!`);
    return response.data;
  },
  activateUser: async (userId: number) => {
    console.log(`Activating user ${userId}`);
    const response: any = await api.post<User>(`/users/${userId}/status`);
    if (response.data)
      notifyMessage(`User activated!`);
    return response.data;
  },
  deactivateUser: async (userId: number) => {
    console.log(`Deactivating user ${userId}`);
    const response: any = await api.delete(`/users/${userId}/status`);
    if (response.data)
      notifyMessage(`User deactivated!`);
    return response.data;
  },
  promoteManager: async (userId: number) => {
    console.log(`Promoting user ${userId} to manager`);
    const response: any = await api.post<User>(`/managers/${userId}`);
    if (response.data)
      notifyMessage(`User promoted to manager!`);
    return response.data;
  },
  demoteManager: async (userId: number) => {
    console.log(`Demoting user ${userId} from manager`);
    const response: any = await api.delete<void>(`/managers/${userId}`);
    if (response.data)
      notifyMessage(`User demoted from manager!`);
    return response.data;
  },
  promoteSalesman: async (userId: number) => {
    console.log(`Promoting user ${userId} to salesman`);
    const response: any = await api.post<User>(`/salesmen/${userId}`);
    if (response.data)
      notifyMessage(`User promoted to salesman!`);
    return response.data;
  },
  demoteSalesman: async (userId: number) => {
    console.log(`Demoting user ${userId} from salesman`);
    const response: any = await api.delete<void>(`/salesmen/${userId}`);
    if (response.data)
      notifyMessage(`User demoted from salesman!`);
    return response.data;
  },
  promoteProvider: async (userId: number) => {
    console.log(`Promoting user ${userId} to provider`);
    const response: any = await api.post<User>(`/providers/${userId}`);
    if (response.data)
      notifyMessage(`User promoted to provider!`);
    return response.data;
  },
  demoteProvider: async (userId: number) => {
    console.log(`Demoting user ${userId} from provider`);
    const response: any = await api.delete<void>(`/providers/${userId}`);
    if (response.data)
      notifyMessage(`User demoted from provider!`);
    return response.data;
  },
  deleteSchedule: async (userId: number, ruleId: number) => {
    console.log(`Removing schedule from user ${userId}`);
    const response: any = await api.delete(`/users/${userId}/schedule/${ruleId}`);
    if (response.data)
      notifyMessage(`Schedule deleted!`);
    return response.data;
  },
  deletePto: async (userId: number, ptoId: number) => {
    console.log(`Removing pto from user ${userId}`);
    const response: any = await api.delete(`/users/${userId}/pto/${ptoId}`);
    if (response.data)
      notifyMessage(`Pto deleted!`);
    return response.data;
  },
  listRecipients: async (messageId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing recipients for message ${messageId}`);
    const response: any = await api.get<User[]>(`/messages/${messageId}/users`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listSalesByProduct: async (productId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing sales for product ${productId}`);
    const response: any = await api.get<Sale[]>(`/products/${productId}/sales`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listPurchasesByProduct: async (productId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing purchases for product ${productId}`);
    const response: any = await api.get<Purchase[]>(`/products/${productId}/purchases`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listInvitedUsers: async (meetingId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing invited users for meeting ${meetingId}`);
    const response: any = await api.get<User[]>(`/meetings/${meetingId}/users`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMeetingFiles: async (meetingId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing files for meeting ${meetingId}`);
    const response: any = await api.get<File[]>(`/meetings/${meetingId}/files`, {params: {page, size, sort, asc}});
    return response.data;
  },
  login: async (email: string, password: string) => {
    console.log(`Logging in with ${email}`);
    const response: any = await api.post('/auth/login', { email, password });
    if (response.data.token)
      notifyMessage(`Welcome back!`);
    return response.data;
  },
  signUp: async (email: string, password: string) => {
    console.log(`Signing up with ${email}`);
    const response: any = await api.post('/auth/signup', { email, password });
    if (response.data.token)
      notifyMessage(`Welcome aboard!`);
    return response.data;
  },
  resetPassword: async (email: string) => {
    console.log(`Resetting password for ${email}`)
    const response: any = await api.post('/auth/reset', { email });
    if (response.data)
      notifyMessage(`Password reset!`);
    return response.data;
  },
  listMeetings: async ({start, end, page, size, sort, asc }: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean }) => {
    console.log(`Listing meetings page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Meeting[]>('/meetings', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listMessages: async ({start, end, page, size, sort, asc }: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean }) => {
    console.log(`Listing messages page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Message[]>('/messages', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listSales: async ({start, end, page, size, sort, asc}: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean}) => {
    console.log(`Listing sales page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Sale[]>('/sales', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listPurchases: async ({start, end, page, size, sort, asc }: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean }) => {
    console.log(`Listing purchases page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Purchase[]>('/purchases', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listWork: async ({start, end, page, size, sort, asc }: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean }) => {
    console.log(`Listing work page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Work[]>('/work', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listSchedule: async ({page, size, sort, asc }: {page: number, size: number, sort: string, asc: boolean}) => {
    console.log(`Listing schedule page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Schedule[]>('/schedule', { params: { page, size, sort, asc } });
    return response.data;
  },
  listPto: async ({start, end, page, size, sort, asc}: {start: Date, end: Date, page: number, size: number, sort: string, asc: boolean }) => {
    console.log(`Listing pto page ${page} size ${size} sort ${sort} asc ${asc}`);
    const response: any = await api.get<Pto[]>('/pto', { params: { start, end, page, size, sort, asc } });
    return response.data;
  },
  listUsers: async ({page, size, sort, asc, query}: {page: number, size: number, sort: string, asc: boolean, query: string}) => {
    console.log(`Listing users page ${page} size ${size} sort ${sort} asc ${asc} query ${query}`);
    const response: any = await api.get<User[]>('/users', { params: { page, size, sort, asc, query } });
    return response.data;
  },
  listProducts: async ({page, size, sort, asc, query}: {page: number, size: number, sort: string, asc: boolean, query: string}) => {
    console.log(`Listing products page ${page} size ${size} sort ${sort} asc ${asc} query ${query}`);
    const response: any = await api.get<Product[]>('/products', { params: { page, size, sort, asc, query } });
    return response.data;
  },
  listMySchedule: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session schedule.`);
    const response: any = await api.get<Schedule[]>(`/auth/schedule`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMyWork: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session work.`);
    const response: any = await api.get<Work[]>(`/auth/work`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMyPto: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session pto.`);
    const response: any = await api.get<Pto[]>(`/auth/pto`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMyMessages: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session messages.`);
    const response: any = await api.get<Message[]>(`/auth/messages`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMyMeetings: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session meetings.`);
    const response: any = await api.get<Meeting[]>(`/auth/meetings`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMySales: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session sales.`);
    const response: any = await api.get<Sale[]>(`/auth/sales`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMyPurchases: async (page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing session purchases.`);
    const response: any = await api.get<Purchase[]>(`/auth/purchases`, {params: {page, size, sort, asc}});
    return response.data;
  },
  getMyProfile: async () => {
    console.log(`Getting session user profile.`);
    const response: any = await api.get<User>(`/auth/profile`);
    return response.data;
  },
  listScheduleRulesByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing schedule rules for user ${userId}`);
    const response: any = await api.get<Schedule[]>(`/users/${userId}/schedule`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listWorkByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing work for user ${userId}`);
    const response: any = await api.get<Work[]>(`/users/${userId}/work`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listPtoByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing pto for user ${userId}`);
    const response: any = await api.get<Pto[]>(`/users/${userId}/pto`, {params: {page, size, sort, asc}});
   return response.data;
  },
  listMessagesByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing messages for user ${userId}`);
    const response: any = await api.get<Sale[]>(`/users/${userId}/messages`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listMeetingsByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing meetings for user ${userId}`);
    const response: any = await api.get<Sale[]>(`/users/${userId}/meetings`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listSalesByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing sales for user ${userId}`);
    const response: any = await api.get<Sale[]>(`/users/${userId}/sales`, {params: {page, size, sort, asc}});
    return response.data;
  },
  listPurchasesByUser: async (userId: number, page: number, size: number, sort: string, asc: boolean) => {
    console.log(`Listing purchases for user ${userId}`);
    const response: any = await api.get<Purchase[]>(`/users/${userId}/purchases`, {params: {page, size, sort, asc}});
    return response.data;
  }
}