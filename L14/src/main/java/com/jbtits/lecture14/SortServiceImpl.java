package com.jbtits.lecture14;

public class SortServiceImpl implements SortService {
    private static final int THREAD_POOL_SIZE = 4;
    private SortControlThread controlThread;

    SortServiceImpl() {
        controlThread = new SortControlThread(THREAD_POOL_SIZE);
        controlThread.start();
    }

    @Override
    public void sort(Number[] source) {
       controlThread.sort(source);
    }

    @Override
    public void dispose() {
        controlThread.kill();
    }
}
