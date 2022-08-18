package project;

import java.util.Comparator;

public class HandComparator implements Comparator<Hand>
{
		@Override
		public int compare(Hand h1, Hand h2) 
		{
			if(h1.getHandType() < h2.getHandType())
			{
				return -1;
			}
			else if(h1.getHandType() > h2.getHandType())
			{
				return 1;
			}
			else
			{
				//Straight Comparison Logic
				if(h1.getHandType() == 2)
				{
					if(h1.getStraightVal() < h2.getStraightVal())
					{
						return -1;
					}
					else if(h1.getStraightVal() > h2.getStraightVal())
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
				//Quads Comparison Logic
				//Get high val between the two for both
				else if(h1.getHandType() == 0)
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getKicker(0) < h2.getKicker(0))
						{
							return -1;
						}
						else if(h1.getKicker(0) > h2.getKicker(0))
						{
							return 1;
						}
						else
						{
							return 0;
						}
					}
				}
				//Full House (Higher Trip then High Pair)
				else if(h1.getHandType() == 1)
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getSecondHighVal() < h2.getSecondHighVal())
						{
							return -1;
						}
						else if(h1.getSecondHighVal() > h2.getSecondHighVal())
						{
							return 1;
						}
						else
						{
							return 0;
						}
					}
				}
				//Three of a Kind
				else if(h1.getHandType() == 3)
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getKicker(0) < h2.getKicker(0))
						{
							return -1;
						}
						else if(h1.getKicker(0) > h2.getKicker(0))
						{
							return 1;
						}
						else
						{
							if(h1.getKicker(1) < h2.getKicker(1))
							{
								return -1;
							}
							else if(h1.getKicker(1) > h2.getKicker(1))
							{
								return 1;
							}
							else
							{
								return 0;
							}
						}
					}
				}
				//Two Pair Comparison Logic
				else if(h1.getHandType() == 4)
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getSecondHighVal() < h2.getSecondHighVal())
						{
							return -1;
						}
						else if(h1.getSecondHighVal() > h2.getSecondHighVal())
						{
							return 1;
						}
						else
						{
							if(h1.getKicker(0) < h2.getKicker(0))
							{
								return -1;
							}
							else if(h1.getKicker(0) > h2.getKicker(0))
							{
								return 1;
							}
							else
							{
								return 0;
							}
						}
					}
				}
				//Pair Comparison Logic
				else if(h1.getHandType() == 5)
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getKicker(0) < h2.getKicker(0))
						{
							return -1;
						}
						else if(h1.getKicker(0) > h2.getKicker(0))
						{
							return 1;
						}
						else
						{
							if(h1.getKicker(1) < h2.getKicker(1))
							{
								return -1;
							}
							else if(h1.getKicker(1) > h2.getKicker(1))
							{
								return 1;
							}
							else
							{
								if(h1.getKicker(2) < h2.getKicker(2))
								{
									return -1;
								}
								else if(h1.getKicker(2) > h2.getKicker(2))
								{
									return 1;
								}
								else
								{
									return 0;
								}
							}
						}
					}
				}
				//High Card Comparison
				else
				{
					if(h1.getHighVal() < h2.getHighVal())
					{
						return -1;
					}
					else if(h1.getHighVal() > h2.getHighVal())
					{
						return 1;
					}
					else
					{
						if(h1.getKicker(0) < h2.getKicker(0))
						{
							return -1;
						}
						else if(h1.getKicker(0) > h2.getKicker(0))
						{
							return 1;
						}
						else
						{
							if(h1.getKicker(1) < h2.getKicker(1))
							{
								return -1;
							}
							else if(h1.getKicker(1) > h2.getKicker(1))
							{
								return 1;
							}
							else
							{
								if(h1.getKicker(2) < h2.getKicker(2))
								{
									return -1;
								}
								else if(h1.getKicker(2) > h2.getKicker(2))
								{
									return 1;
								}
								else
								{
									if(h1.getKicker(3) < h2.getKicker(3))
									{
										return -1;
									}
									else if(h1.getKicker(3) > h2.getKicker(3))
									{
										return 1;
									}
									else
									{
										return 0;
									}
								}
							}
						}
					}
				}
			}
		}
}
