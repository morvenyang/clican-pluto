//
//  StoreRankViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "StoreRankModel.h"

@interface StoreRankViewController : SwitchViewController<StoreRankDelegate>{
    StoreRankModel* _storeRankModel;
    NSMutableArray* _channelLables;
    NSArray* _channels;
    NSMutableArray* _tableViews;
}
@property (nonatomic, retain) StoreRankModel *storeRankModel;
@property (nonatomic, retain) NSMutableArray *channelLables;
@property (nonatomic, retain) NSArray* channels;
@property (nonatomic, retain) NSMutableArray* tableViews;

-(id) initWithBrand:(NSString*) brand;

@end
