//
//  GoodRankViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "GoodRankModel.h"
@interface GoodRankViewController : SwitchViewController{
    GoodRankModel* _goodRankModel;
    NSMutableArray* _goods;
}
@property (nonatomic, retain) GoodRankModel *goodRankModel;
@property (nonatomic, retain) NSMutableArray *goods;
@end
