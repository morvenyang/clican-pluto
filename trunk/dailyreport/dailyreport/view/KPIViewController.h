//
//  KPIViewController.h
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"
#import "KPIModel.h"
@interface KPIViewController : SwitchViewController<KPIDelegate>{
    KPIModel* _kpiModel;
    NSMutableArray* _channelLables;
    NSArray* _channels;
    UILabel* _dayAmountLabel;
    UILabel* _docNumberLabel;
    UILabel* _avgDocCountLabel;
    UILabel* _avgPriceLabel;
    UILabel* _apsLabel;
}

@property (nonatomic, retain) KPIModel *kpiModel;
@property (nonatomic, retain) NSArray *channels;
@property (nonatomic, retain) NSMutableArray *channelLables;
@property (nonatomic, retain) UILabel *dayAmountLabel;
@property (nonatomic, retain) UILabel *docNumberLabel;
@property (nonatomic, retain) UILabel *avgDocCountLabel;
@property (nonatomic, retain) UILabel *avgPriceLabel;
@property (nonatomic, retain) UILabel *apsLabel;
-(id) initWithBrand:(NSString*) brand;
@end
