//
//  StoreRankViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "StoreRankModel.h"
#import "ChannelRank.h"
@interface StoreRankViewController : SwitchViewController<StoreRankDelegate>{
    StoreRankModel* _storeRankModel;
    NSMutableArray* _channelLables;
    NSArray* _channels;
    NSMutableArray* _tableViews;
    CGFloat _tableOffset;
    UILabel* _topLabel;
    UILabel* _bottomLabel;
    NSString* _order;
    ChannelRank* _selectedChanngelRank;
}
@property (nonatomic, retain) StoreRankModel *storeRankModel;
@property (nonatomic, retain) NSMutableArray *channelLables;
@property (nonatomic, retain) NSArray* channels;
@property (nonatomic, retain) NSMutableArray* tableViews;
@property (nonatomic, retain) UILabel* topLabel;
@property (nonatomic, retain) UILabel* bottomLabel;
@property (nonatomic, retain) NSString* order;
@property (nonatomic, retain) ChannelRank* selectedChanngelRank;
-(id) initWithBrand:(NSString*) brand;

@end
