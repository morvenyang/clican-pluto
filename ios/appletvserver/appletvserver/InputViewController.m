//
//  InputViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "InputViewController.h"

@implementation InputViewController

@synthesize label = _label;
@synthesize instruction = _instruction;
@synthesize initialText = _initialText;
@synthesize callback = _callback;
@synthesize ctx = _ctx;
@synthesize textField = _textField;
@synthesize submitButton = _submitButton;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(id) initWithLabel:(NSString*) label instruction:(NSString*)instruction initialText:(NSString*) initialText callback:(JSObjectRef) callback ctx:(JSContextRef) ctx{
    self = [super init];
    if(self){
        self.label=label;
        self.instruction = instruction;
        self.initialText = initialText;
        self.callback = callback;
        self.ctx = ctx;
        
        self.textField = [[UITextField alloc] init];
        
        self.textField.font = [UIFont fontWithName:@"Microsoft YaHei" size:14];
        self.textField.textColor = RGBCOLOR(204,204,204);
        
		self.textField.autocorrectionType = UITextAutocorrectionTypeNo;
        self.textField.keyboardType = UIKeyboardTypeDefault;
		self.textField.returnKeyType = UIReturnKeyDone;
		
		self.textField.clearButtonMode = UITextFieldViewModeAlways;
		self.textField.delegate = self;
		[self.textField setAccessibilityLabel:self.label];
    }
    return self;
}



- (void)dealloc
{

    TT_RELEASE_SAFELY(_label);
    TT_RELEASE_SAFELY(_instruction);
    TT_RELEASE_SAFELY(_initialText);
    TT_RELEASE_SAFELY(_textField);
    TT_RELEASE_SAFELY(_submitButton);
    [super dealloc];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void) submitAction
{
    NSLog(@"Submit");
    NSString* content = self.textField.text;
    NSLog(@"content:%@" ,content);
    JSValueRef args[1];
    args[0] = JSValueMakeString(self.ctx,JSStringCreateWithUTF8CString([content UTF8String]));
    JSObjectCallAsFunction(self.ctx,self.callback,NULL,1,args,NULL);
}

- (void)createModel {
    self.dataSource = [TTSectionedDataSource dataSourceWithObjects:
                       @"",
                       [TTTableControlItem itemWithCaption:self.instruction control:nil],
                       [TTTableControlItem itemWithCaption:self.label control:self.textField],
                       nil];
}

- (void)loadView
{
    [super loadView];
    self.submitButton = [UIButton buttonWithType:UIButtonTypeCustom];
    self.submitButton.contentMode = UIViewContentModeCenter;
    [self.submitButton addTarget:self action:@selector(submitAction) forControlEvents: UIControlEventTouchUpInside];
    self.submitButton.frame = CGRectMake(24, 27+125+80, 50, 50);
    [self.submitButton setTitle:@"提交" forState:UIControlStateNormal];
    self.submitButton.backgroundColor = RGBCOLOR(256,20,147);
    [self.submitButton sizeToFit];
    [self.view addSubview:self.submitButton];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
