//
//  NoRetailsController.h
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "NoRetailModel.h"
@interface NoRetailsController : SwitchViewController<NoRetailDelegate>{
    NoRetailModel* _noRetailModel;
    CGFloat _tableOffset;
    NSMutableArray* _channelLables;
    NSArray* _noRetails;
    NSMutableArray* _tableViews;
    UILabel* _storeNameLabel;
}
@property (nonatomic, retain) NoRetailModel *noRetailModel;
@property (nonatomic, retain) NSMutableArray *channelLables;
@property (nonatomic, retain) NSArray *noRetails;
@property (nonatomic, retain) NSMutableArray* tableViews;
@property (nonatomic, retain) UILabel* storeNameLabel;
-(id) initWithBrand:(NSString*) brand;
@end
