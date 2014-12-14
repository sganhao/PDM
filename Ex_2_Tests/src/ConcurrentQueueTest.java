import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class ConcurrentQueueTest {

	private final int NR_PUT_THREADS = 50;
	private final int NR_TAKE_THREADS = 89;
	private final int REPETITIONS = 2;
	private final int SLEEP_TIME = 0;

	private ConcurrentQueue<String> queue = new ConcurrentQueue<String>();
	private final CountDownLatch countDownLatch = new CountDownLatch((NR_PUT_THREADS + NR_TAKE_THREADS) * REPETITIONS);

	private volatile int _putDone = 0;
	private volatile int _takeDone = 0;

	private final TestItem[] _data = initData();

	public void putThreadMethod () {
		if (_putDone == _data.length) return;
		String dataToPut = _data[_putDone++].data;
		queue.put(dataToPut);

		assertTrue(true);
		countDownLatch.countDown();		
	}

	public void tryTakeMethod() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String res = queue.tryTake();

		if (res != null) {
			TestItem item = Arrays.asList(_data).get(Arrays.asList(_data).indexOf(res));
			item.returned =  true;
			_takeDone++;
		}
		countDownLatch.countDown();
	}

	@Test
	public void concurrentQueue_Test() {
		int higher, lower;

		if (NR_TAKE_THREADS > NR_PUT_THREADS) {
			higher = NR_TAKE_THREADS * REPETITIONS;
			lower = NR_PUT_THREADS * REPETITIONS;

			for (int i = 0; i < higher; i++) {
				new Thread(() -> tryTakeMethod()).start();
				if (i < lower)
					new Thread(() -> putThreadMethod()).start();
			}
		} else {
			higher = NR_PUT_THREADS * REPETITIONS;
			lower = NR_TAKE_THREADS * REPETITIONS;

			for (int i = 0; i < higher; i++) {
				new Thread(() -> putThreadMethod()).start();
				if (i < lower)
					new Thread(() -> tryTakeMethod()).start();
			}
		}

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String res = null;
		int takedElems = countTakedElemes();
		assertEquals(_takeDone, takedElems);
		System.out.println("Number of threads: " + 
				NR_TAKE_THREADS + NR_PUT_THREADS + 
				"\n\tPut threads: " + _putDone + 
				"\n\tTryTake threads: " + _takeDone);		
	}

	private int countTakedElemes() {
		int takedElems = 0;
		for (int i = 0; i < _data.length; i++) {
			if (_data[i].returned)
				takedElems++;
		}
		return takedElems;
	}	

	public static TestItem[] initData() {
		TestItem[] items = new TestItem[15];
		for (int i = 0; i < 15; i++) {
			items[i] = new TestItem("" + i);
		}
		return items;
	}
}
