# Grocery Simulator 5.2

The till and your balance are now two separate places, and banking the takings takes as long as it should.

## Your money is counted once

- A cash sale used to be added to your balance and put into the till at the same time, so depositing the till counted the same money twice. Cash sales now only fill the till, and the money reaches your balance when you bank it.
- The morning no longer takes the float out of your balance either. The till and the balance are two separate places, and money only moves between them when you move it.

## Banking the takings

- Deposit Cash opens its own screen. Choose the notes and coins yourself, or take everything above the float, or the lot.
- The cash leaves the till straight away and shows as a Pending Cash Deposit. Your balance is credited about forty minutes later, when it clears.
- You can also withdraw to the till when it is running short. The money leaves your balance at once and the notes arrive about thirty minutes later.
- A deposit that is still on its way is remembered when you save and load, and anything already due is settled as soon as you come back.

## Smaller fixes

- A cash customer who walks out before paying now takes their money back out of the till.
- The few rupees a customer waves away are tracked, so the till still balances at the end of the day.
